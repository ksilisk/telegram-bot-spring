package io.ksilisk.telegrambot.core.handler.update;

/**
 * Handles a single Telegram update of type {@link U}.
 *
 * <p>Handlers are invoked by the dispatching pipeline after routing.
 * Calls may occur concurrently depending on the executor configuration,
 * therefore, implementations should be thread-safe or otherwise guard
 * shared state explicitly.</p>
 *
 * @param <U> the type of value being handled
 */
public interface Handler<U> {
    /**
     * Process the given update.
     *
     * @param update never {@code null}
     */
    void handle(U update);
}
