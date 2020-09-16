package com.github.egorklimov.engine.executor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


/**
 * This supplier provides cached and bounded executor service.
 *
 * Motivation: predefined executors from {@code java.util.Executors} supports cached thread pool
 * with unbounded thread count and thread pool with fixed thread count.
 */
@ApplicationScoped
public class CachedAndBoundedThreadPoolExecutorSupplier implements Supplier<ThreadPoolExecutor> {

    private final ExecutorConfiguration executorConfiguration;

    @Inject
    public CachedAndBoundedThreadPoolExecutorSupplier(ExecutorConfiguration executorConfiguration) {
        this.executorConfiguration = executorConfiguration;
    }

    @Override
    public ThreadPoolExecutor get() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                executorConfiguration.getCorePoolSize(),
                executorConfiguration.getAdditionalPoolSize(),
                2,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>()
        );
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }
}
