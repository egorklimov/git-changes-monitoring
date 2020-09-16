package com.github.egorklimov.engine.executor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class WrappedTask implements Runnable {

    private final Runnable onStart;
    private final Runnable runnable;
    private final Runnable onExit;

    @Override
    public void run() {
        try {
            onStart.run();
            runnable.run();
        } catch (Exception e) {
            log.error("Error during task execution", e);
        } finally {
            onExit.run();
        }
    }
}
