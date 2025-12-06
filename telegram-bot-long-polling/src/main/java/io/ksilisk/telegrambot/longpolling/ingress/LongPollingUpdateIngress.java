package io.ksilisk.telegrambot.longpolling.ingress;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.ingress.UpdateIngress;

/**
 * An ingress that actively retrieves Telegram {@link Update} objects using the
 * long polling mechanism.
 * <p>
 * Implementations typically manage an internal polling loop and submit
 * retrieved updates into the processing pipeline. Unlike webhook-based
 * ingress, long polling ingress owns its own worker threads and must control
 * their lifecycle explicitly.
 * <p>
 * Both lifecycle methods {@link #start()} and {@link #stop()} are expected to
 * be idempotent and thread-safe.
 */
public interface LongPollingUpdateIngress extends UpdateIngress {
    /**
     * Starts the long polling process.
     * <p>
     * Implementations may spawn worker threads, schedule tasks, or perform
     * other initialization required to begin polling.
     * Calling this method multiple times must not cause duplicate polling loops.
     */
    void start();

    /**
     * Stops the long polling process and performs any required cleanup.
     * <p>
     * Implementations should terminate blocking operations, shut down executor
     * services, and ensure that polling threads exit gracefully.
     * Calling this method multiple times must be safe.
     */
    void stop();
}
