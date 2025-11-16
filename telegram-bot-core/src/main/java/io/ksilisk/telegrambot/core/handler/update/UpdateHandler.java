package io.ksilisk.telegrambot.core.handler.update;

import com.pengrad.telegrambot.model.Update;

/**
 * Handles a single Telegram {@link Update}.
 *
 * <p>Handlers are invoked by the dispatching pipeline after routing.
 * Calls may occur concurrently depending on the executor configuration,
 * therefore, implementations should be thread-safe or otherwise guard
 * shared state explicitly.</p>
 */
public interface UpdateHandler {
    /**
     * Process the given update.
     *
     * @param update never {@code null}
     */
    void handle(Update update);
}
