package io.ksilisk.telegrambot.core.handler.exception;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.selector.ExceptionHandlerSelector;

/**
 * Handles exceptions that occur during update processing.
 *
 * <p>Whether a handler will be invoked is determined externally by an
 * {@link ExceptionHandlerSelector}. Implementations only declare whether
 * they are able to handle a given exception through {@link #supports(Throwable, Update)}
 * and perform the handling logic in {@link #handle(Throwable, Update)}.</p>
 *
 * <p>If {@link #terminal()} returns {@code true}, selector implementations may
 * choose to stop processing additional handlers after this one.</p>
 */
public interface ExceptionHandler {
    /**
     * Determine whether this handler can process the given exception.
     *
     * @param t the thrown exception, never {@code null}
     * @param update the update being processed when the exception occurred,
     *               may be {@code null}
     * @return {@code true} if this handler supports the given exception
     */
    boolean supports(Throwable t, Update update);

    /**
     * Handle the given exception.
     *
     * @param t the thrown exception, never {@code null}
     * @param update the related update, may be {@code null}
     */
    void handle(Throwable t, Update update);

    /**
     * A human-readable handler name.
     * Defaults to the simple class name.
     */
    default String name() {
        return getClass().getSimpleName();
    }


    /**
     * Indicates whether this handler is terminal.
     *
     * <p>If {@code true}, selectors may stop selecting further handlers
     * once this one is chosen.</p>
     */
    default boolean terminal() {
        return false;
    }
}
