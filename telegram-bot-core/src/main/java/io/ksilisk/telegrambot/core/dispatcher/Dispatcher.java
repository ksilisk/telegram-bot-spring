package io.ksilisk.telegrambot.core.dispatcher;

/**
 * Dispatches a single update of type {@link U} to the routing and handler pipeline.
 *
 * <p>The framework may invoke this method concurrently from multiple threads.
 * Implementations must be thread-safe.</p>
 *
 * @param <U> the type of value being dispatched
 */
public interface Dispatcher<U> {
    /**
     * Dispatch the given value.
     *
     * @param value never {@code null}
     */
    void dispatch(U value);
}
