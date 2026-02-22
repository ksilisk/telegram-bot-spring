package io.ksilisk.telegrambot.core.handler.exception;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.exception.handler.ExceptionHandlerExecutionException;
import io.ksilisk.telegrambot.core.selector.UpdateExceptionHandlerSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CompositeUpdateExceptionHandler implements UpdateExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(CompositeUpdateExceptionHandler.class);

    private final List<UpdateExceptionHandler> updateExceptionHandlers;
    private final UpdateExceptionHandlerSelector exceptionHandlerSelector;
    private final ExceptionHandlerErrorPolicy errorPolicy;

    public CompositeUpdateExceptionHandler(List<UpdateExceptionHandler> updateExceptionHandlers,
                                           UpdateExceptionHandlerSelector exceptionHandlerSelector,
                                           ExceptionHandlerErrorPolicy errorPolicy) {
        this.updateExceptionHandlers = updateExceptionHandlers;
        this.exceptionHandlerSelector = exceptionHandlerSelector;
        this.errorPolicy = errorPolicy;
    }

    @Override
    public boolean supports(Throwable t, Update update) {
        return true;
    }

    @Override
    public void handle(Throwable t, Update update) {
        List<UpdateExceptionHandler> selectedHandlers = exceptionHandlerSelector.select(this.updateExceptionHandlers, t, update);
        for (UpdateExceptionHandler updateExceptionHandler : selectedHandlers) {
            try {
                if (updateExceptionHandler.supports(t, update)) {
                    log.debug("Invoking ExceptionHandler: '{}'", updateExceptionHandler.name());
                    updateExceptionHandler.handle(t, update);
                    if (updateExceptionHandler.terminal()) {
                        return;
                    }
                }
            } catch (Exception ex) {
                switch (errorPolicy) {
                    case LOG -> log.warn(
                            "ExceptionHandler '{}' failed while catching throwable '{}' " +
                                    "for update (id={}). Ignoring due to ErrorPolicy=LOG",
                            updateExceptionHandler.name(), t, update.updateId(), ex);
                    case THROW -> throw new ExceptionHandlerExecutionException(ex);
                }
            }
        }
    }
}
