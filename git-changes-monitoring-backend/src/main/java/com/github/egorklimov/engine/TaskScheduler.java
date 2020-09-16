package com.github.egorklimov.engine;

import com.github.egorklimov.data.repository.branch.Branch;
import com.github.egorklimov.data.repository.branch.BranchRepository;
import com.github.egorklimov.data.repository.commit.CommitRepository;
import com.github.egorklimov.data.repository.contributor.ContributorRepository;
import com.github.egorklimov.data.repository.gitrepository.GitRepository;
import com.github.egorklimov.data.repository.gitrepository.GitRepositoryRepository;
import com.github.egorklimov.data.transaction.Transaction;
import com.github.egorklimov.engine.executor.BranchTaskIdentifier;
import com.github.egorklimov.engine.executor.ExecutorService;
import com.github.egorklimov.engine.executor.FetchRepository;
import com.github.egorklimov.engine.executor.ListAllBranches;
import com.github.egorklimov.engine.executor.RepositoryTaskIdentifier;
import com.github.egorklimov.engine.task.PersistAllReceivedCommits;
import com.github.egorklimov.engine.task.ProgressHandler;
import com.github.egorklimov.entity.BranchUpdate;
import com.github.egorklimov.entity.Commit;
import com.github.egorklimov.entity.RepositoryUpdate;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@ApplicationScoped
class TaskScheduler {

    private final Transaction transaction;
    private final BranchRepository branchRepository;
    private final CommitRepository commitRepository;
    private final GitRepositoryRepository repository;
    private final ContributorRepository contributorRepository;

    private final ProgressHandler<List<Commit>> processedCommitsHandler;
    private final ExecutorService executorService;

    @Inject
    public TaskScheduler(Transaction transaction,
                         BranchRepository branchRepository,
                         CommitRepository commitRepository,
                         GitRepositoryRepository repository,
                         ContributorRepository contributorRepository,
                         ExecutorService executorService) {
        this.repository = repository;
        this.transaction = transaction;
        this.branchRepository = branchRepository;
        this.commitRepository = commitRepository;
        this.contributorRepository = contributorRepository;
        this.executorService = executorService;

        this.processedCommitsHandler = new PersistAllReceivedCommits(transaction, commitRepository);
    }

    @Scheduled(every = "{engine.sync_interval}")
    void synchronizeRepositories() {
        log.info("Started repository synchronization");
        List<GitRepository> syncingRepositories = repository.syncingRepositories();
        // lock repositories
        syncingRepositories.forEach(r ->
                transaction.execute(() ->
                        repository.update("is_syncing = false where id = ?1", r.getId())
                )
        );

        syncingRepositories.forEach(repositoryToSync -> {
            log.info("Repository[{}]: start", repositoryToSync.getPath());
            try (Git git = Git.open(new File(repositoryToSync.getPath()))) {
                RepositoryUpdate update = new FetchRepository(git, repositoryToSync.getId()).get();
                log.info("Repository[{}]: fetched {}", repositoryToSync.getPath(), update);
                List<Branch> forcePushedBranches =
                        update.getForcePushedBranches()
                                .stream()
                                .map(BranchUpdate::getBranch)
                                .collect(Collectors.toList());
                if (!forcePushedBranches.isEmpty()) {
                    log.info("Repository[{}]: delete force pushed branches {}", repositoryToSync.getPath(), forcePushedBranches);
                    new DeleteTargetBranches(forcePushedBranches, transaction, branchRepository).apply();
                }

                List<Branch> actualBranches =
                        new ListAllBranches(git, repositoryToSync.getId()).get()
                                .stream()
                                .map(new LoadBranchOrPersistIfNotExists(transaction, branchRepository))
                                .collect(Collectors.toList());
                log.info("Repository[{}]: actual branches {}", repositoryToSync.getPath(), actualBranches);

                executorService.interruptInactiveBranches(
                        new RepositoryTaskIdentifier(repositoryToSync),
                        actualBranches.stream()
                                .map(BranchTaskIdentifier::new)
                                .map(BranchTaskIdentifier::get)
                                .collect(Collectors.toSet())
                );
                log.info("Repository[{}]: delete inactive branches", repositoryToSync.getPath());
                new DeleteInactiveBranches(repositoryToSync.getId(), actualBranches, transaction, branchRepository).apply();

                branchRepository.notScanned(repositoryToSync.getId())
                        .forEach(notScannedBranch -> {
                            log.info("Repository[{}]: started to scan branch {}", repositoryToSync.getPath(), notScannedBranch);
                            executorService.submitTask(
                                    repositoryToSync,
                                    notScannedBranch,
                                    new ScanAllCommitsFromBranch(
                                            git,
                                            notScannedBranch,
                                            transaction.execute(() -> commitRepository.countByBranch(notScannedBranch.getId())),
                                            new PersistContributorIfNotExists(transaction, contributorRepository),
                                            branch -> transaction.execute(() -> branchRepository.update("is_scanned = true where id = ?1", branch.getId()))
                                    )
                            );
                        });

                update.getFastForwardedBranches()
                        .forEach(updatedBranch -> {
                            log.info("Repository[{}]: started to update branch {}", repositoryToSync.getPath(), updatedBranch);
                            Branch resolvedBranch = new LoadBranchOrPersistIfNotExists(transaction, branchRepository).apply(updatedBranch.getBranch());
                            executorService.submitTask(
                                    repositoryToSync,
                                    resolvedBranch,
                                    new ScanAllFastForwardedCommits(
                                            git,
                                            resolvedBranch,
                                            updatedBranch.getOldHash(),
                                            new PersistContributorIfNotExists(transaction, contributorRepository)
                                    )
                            );
                        });
            } catch (IOException e) {
                log.error("Repository[{}]: synchronization failed", repositoryToSync.getPath(), e);
            }
            // unlock
            transaction.execute(() -> repository.update("is_syncing = true where id = ?1", repositoryToSync.getId()));
        });
    }

    /**
     * Each {@code engine.sync_interval} seconds polls results of each registered task
     * and executes {@code processedCommitsHandler} on them.
     */
    @Scheduled(every = "{engine.sync_interval}")
    void publishChanges() {
        log.info("Started change publishing");
        executorService.pollAll().forEach((repositoryPath, taskPerBranch) -> {
            log.info("Repository[{}] has {} active branches", repositoryPath, taskPerBranch.size());
            taskPerBranch.forEach((branchName, commits) -> {
                try {
                    log.info(
                            "Received {} commits on branch [repo:{}/branch:{}]",
                            commits.size(),
                            repositoryPath,
                            branchName
                    );

                    processedCommitsHandler.accept(commits);

                    log.info(
                            "Successfully processed received commits for [repo:{}/branch:{}]",
                            repositoryPath,
                            branchName
                    );
                } catch (Exception e) {
                    log.error(
                            "Failed to process received commits for [repo:{}/branch:{}]",
                            repositoryPath,
                            branchName,
                            e
                    );
                }
            });
        });
    }
}
