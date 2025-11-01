package io.ksilisk.telegrambot.core.handler.exception;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.exception.handler.ExceptionHandlerExecutionException;
import io.ksilisk.telegrambot.core.logger.BotLogger;
import io.ksilisk.telegrambot.core.selector.ExceptionHandlerSelector;

import java.util.List;

public class CompositeExceptionHandler implements ExceptionHandler {
    private final List<ExceptionHandler> exceptionHandlers;
    private final ExceptionHandlerSelector exceptionHandlerSelector;
    private final ExceptionHandlerErrorPolicy errorPolicy;
    private final BotLogger log;

    public CompositeExceptionHandler(List<ExceptionHandler> exceptionHandlers,
                                     ExceptionHandlerSelector exceptionHandlerSelector,
                                     ExceptionHandlerErrorPolicy errorPolicy,
                                     BotLogger log) {
        this.exceptionHandlers = exceptionHandlers;
        this.exceptionHandlerSelector = exceptionHandlerSelector;
        this.errorPolicy = errorPolicy;
        this.log = log;
    }

    @Override
    public boolean supports(Throwable t, Update update) {
        return true;
    }

    @Override
    public void handle(Throwable t, Update update) {
        List<ExceptionHandler> selectedHandlers = exceptionHandlerSelector.select(this.exceptionHandlers, t, update);
        for (ExceptionHandler exceptionHandler : selectedHandlers) {
            try {
                if (exceptionHandler.supports(t, update)) {
                    exceptionHandler.handle(t, update);
                    if (exceptionHandler.terminal()) {
                        return;
                    }
                }
            } catch (Exception ex) {
                switch (errorPolicy) {
                    case LOG -> log.error("ExceptionHandler '{}' failed.", ex, exceptionHandler.name());
                    case THROW -> throw new ExceptionHandlerExecutionException(ex);
                }
            }
        }
    }
}
