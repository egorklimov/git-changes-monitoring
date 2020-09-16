package com.github.egorklimov.engine.executor;

import com.github.egorklimov.engine.task.PollableTask;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class TaskQueue<T> implements PollableTask<T> {

    private static final AtomicLong taskQueueCounter = new AtomicLong(0);
    private final AtomicBoolean interrupted = new AtomicBoolean(false);
    private final Queue<PollableTask<T>> queue = new ArrayDeque<>();
    private final BlockingQueue<T> processed = new LinkedBlockingQueue<>();
    private final long queueId;

    public TaskQueue() {
        queueId = taskQueueCounter.incrementAndGet();
    }

    public void add(PollableTask<T> next) {
        if (!interrupted.get()) {
            queue.add(next);
        }
    }

    @Override
    public List<T> poll() {
        log.info("TaskQueue[{}]: pooling changes: queue - {}, changes - {}", queueId, queue.size(), processed.size());
        List<T> buffer = new ArrayList<>();
        processed.drainTo(buffer);
        return Collections.unmodifiableList(buffer);
    }

    @SneakyThrows
    @Override
    public void run() {
        log.info("TaskQueue[{}]: starting task queue with {} tasks", queueId, queue.size());
        while (!queue.isEmpty() && !interrupted.get()) {
            queue.peek().run();
            processed.addAll(queue.peek().poll());
            queue.remove();
            log.info("TaskQueue[{}]: processed task, queue: {}", queueId, queue.size());
        }

        while (!processed.isEmpty() && !interrupted.get()) {
            log.info("TaskQueue[{}]: waiting for polling changes - {}", queueId, processed.size());
            TimeUnit.SECONDS.sleep(1);
        }
        log.info("TaskQueue[{}]: whole queue finished: queue - {}, changes - {}", queueId, queue.size(), processed.size());
    }

    @Override
    public void interrupt() {
        log.info("TaskQueue[{}]: queue interrupted: queue - {}, changes - {}", queueId, queue.size(), processed.size());
        interrupted.set(true);
        PollableTask<T> activeTask = queue.peek();
        queue.clear();
        Optional.ofNullable(activeTask)
                .ifPresent(PollableTask::interrupt);
    }
}
