package com.github.egorklimov.engine.executor;

import com.github.egorklimov.data.repository.branch.Branch;
import com.github.egorklimov.data.repository.gitrepository.GitRepository;
import com.github.egorklimov.engine.task.PollableTask;
import com.github.egorklimov.entity.Commit;
import io.quarkus.runtime.ShutdownEvent;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class ExecutorService {

    private final ThreadPoolExecutor threadPoolExecutor;
    private final Map<String, Map<String, TaskQueue<Commit>>> registeredTasks;

    @Inject
    public ExecutorService(CachedAndBoundedThreadPoolExecutorSupplier threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor.get();
        this.registeredTasks = new ConcurrentHashMap<>();
    }

    /**
     * Gracefully stops the thread pool.
     * @param ev  Quarkus ShutdownEvent
     */
    void onStop(@Observes ShutdownEvent ev) {
        log.info("Service is stopping...");
        log.info("Active tasks: {}", threadPoolExecutor.getActiveCount());
        List<Runnable> hungTasks = new ArrayList<>();
        threadPoolExecutor.shutdown();
        try {
            registeredTasks.values().forEach(byBranch -> byBranch.values().forEach(TaskQueue::interrupt));
            if (!threadPoolExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                hungTasks.addAll(threadPoolExecutor.shutdownNow());
            }
        } catch (InterruptedException e) {
            hungTasks.addAll(threadPoolExecutor.shutdownNow());
        } finally {
            log.info("Executor stopped, count of hung tasks: {}", hungTasks.size());
        }
    }


    public void submitTask(GitRepository repository, Branch branch, PollableTask<Commit> task) {
        log.info("Submitting task for repository[{}], branch[{}]", repository.getPath(), branch);
        String repositoryTaskKey = new RepositoryTaskIdentifier(repository).get();

        var tasksPerBranch = registeredTasks.computeIfAbsent(
                repositoryTaskKey,
                any -> new ConcurrentHashMap<>()
        );

        String branchTaskKey = new BranchTaskIdentifier(branch).get();
        if (tasksPerBranch.containsKey(branchTaskKey)) {
            log.info("Branch[{}] has a working queue => appending task", branchTaskKey);
            tasksPerBranch.get(branchTaskKey)
                    .add(task);
        } else {
            log.info("Branch[{}] has no active tasks, creating queue", branchTaskKey);
            TaskQueue<Commit> branchQueue = new TaskQueue<>();
            branchQueue.add(task);
            threadPoolExecutor.submit(new WrappedTask(
                    () -> tasksPerBranch.put(branchTaskKey, branchQueue),
                    branchQueue::run,
                    () -> tasksPerBranch.remove(branchTaskKey)
            ));
        }
    }

    public Map<String, Map<String, List<Commit>>> pollAll() {
        HashMap<String, Map<String, List<Commit>>> result = new HashMap<>();
        registeredTasks.forEach((repository, branch) ->
            result.putIfAbsent(
                    repository,
                    branch.entrySet()
                            .stream()
                            .collect(
                                    Collectors.toMap(
                                            Map.Entry::getKey,
                                            taks -> taks.getValue()
                                                    .poll()
                                    )
                            )
            )
        );
        return Collections.unmodifiableMap(result);
    }

    public void interruptInactiveBranches(Supplier<String> repositoryKey, Set<String> allowedKeys) {
        registeredTasks
                .getOrDefault(repositoryKey.get(), Collections.emptyMap())
                .entrySet()
                .stream()
                .filter(pair -> allowedKeys.contains(pair.getKey()))
                .map(Map.Entry::getValue)
                .forEach(TaskQueue::interrupt);
    }
}
