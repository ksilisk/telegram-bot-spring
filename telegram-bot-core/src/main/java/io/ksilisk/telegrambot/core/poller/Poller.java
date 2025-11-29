package io.ksilisk.telegrambot.core.poller;

/**
 * Entry point for receiving updates in long-polling mode.
 * Implementations should make {@link #start()} and {@link #stop()} idempotent.</p>
 *
 * @param <U> the type of value being polled
 */
public interface Poller<U> {
    /**
     * Start polling for updates.
     * Multiple invocations should be safe and have no additional effect.
     */
    void start();

    /**
     * Stop polling and release any allocated resources.
     * Subsequent calls should be safe.
     */
    void stop();

}
