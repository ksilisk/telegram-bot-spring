package io.ksilisk.telegrambot.core.poller;

/**
 * Entry point for receiving updates in long-polling mode.
 *
 * <p>A poller starts a background process that continuously requests updates
 * from the Telegram Bot API and forwards them to the delivery pipeline.
 * Implementations should make {@link #start()} and {@link #stop()} idempotent.</p>
 */
public interface UpdatePoller {
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
