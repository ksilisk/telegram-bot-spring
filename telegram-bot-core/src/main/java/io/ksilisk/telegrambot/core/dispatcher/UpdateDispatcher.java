package io.ksilisk.telegrambot.core.dispatcher;

import com.pengrad.telegrambot.model.Update;

/**
 * Dispatches a single {@link Update} to the routing and handler pipeline.
 *
 * <p>The framework may invoke this method concurrently from multiple threads.
 * Implementations must be thread-safe.</p>
 */
public interface UpdateDispatcher {
    /**
     * Dispatch the given update.
     *
     * @param update never {@code null}
     */
    void dispatch(Update update);
}
