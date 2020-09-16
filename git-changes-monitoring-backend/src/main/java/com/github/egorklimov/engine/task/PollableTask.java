package com.github.egorklimov.engine.task;

import java.util.List;

/**
 * The PollableTask interface describes a task which supports removal of already processed tasks.
 * @param <T>   type of the processed value
 */
public interface PollableTask<T> {
    List<T> poll();
    void run();
    void interrupt();
}
