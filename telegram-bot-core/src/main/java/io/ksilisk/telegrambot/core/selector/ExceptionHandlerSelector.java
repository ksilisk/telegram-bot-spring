package io.ksilisk.telegrambot.core.selector;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.exception.ExceptionHandler;

import java.util.List;

/**
 * Selects the exception handlers that should process a given exception.
 *
 * <p>This component is responsible for determining which handlers should be
 * invoked, in which order, and whether terminal handlers stop further selection.</p>
 *
 * <p>The returned list defines the exact invocation order for the handlers.</p>
 */
@FunctionalInterface
public interface ExceptionHandlerSelector {
    /**
     * Select handlers for the given exception and update.
     *
     * @param exceptionHandlers all available handlers, never {@code null}
     * @param t the thrown exception, never {@code null}
     * @param update the related update, may be {@code null}
     * @return ordered handlers to invoke, never {@code null}
     */
    List<ExceptionHandler> select(List<ExceptionHandler> exceptionHandlers, Throwable t, Update update);
}
