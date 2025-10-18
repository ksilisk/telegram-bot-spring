package io.ksilisk.telegrambot.core.dispatcher;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.exception.CompositeExceptionHandler;
import io.ksilisk.telegrambot.core.router.UpdateRouter;
import io.ksilisk.telegrambot.core.strategy.CompositeNoMatchStrategy;

import java.util.List;

public class SimpleUpdateDispatcher implements UpdateDispatcher {
    private final List<UpdateRouter> updateRouters;
    private final CompositeExceptionHandler exceptionHandler;
    private final CompositeNoMatchStrategy noMatchStrategy;

    public SimpleUpdateDispatcher(List<UpdateRouter> updateRouters,
                                  CompositeExceptionHandler exceptionHandler,
                                  CompositeNoMatchStrategy noMatchStrategy) {
        this.updateRouters = updateRouters;
        this.exceptionHandler = exceptionHandler;
        this.noMatchStrategy = noMatchStrategy;
    }

    @Override
    public void dispatch(Update update) {
        try {
            for (UpdateRouter updateRouter : updateRouters) {
                if (updateRouter.supports(update)) {
                    if (!updateRouter.route(update)) {
                        noMatchStrategy.handle(update);
                    }
                    return;
                }
            }
            noMatchStrategy.handle(update);
        } catch (Exception ex) {
            exceptionHandler.handle(ex, update);
        }
    }
}
