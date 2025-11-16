package io.ksilisk.telegrambot.core.dispatcher;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.router.UpdateRouter;
import io.ksilisk.telegrambot.core.strategy.CompositeNoMatchStrategy;

import java.util.List;

public class SimpleUpdateDispatcher implements UpdateDispatcher {
    private final List<UpdateRouter> updateRouters;
    private final CompositeNoMatchStrategy noMatchStrategy;

    public SimpleUpdateDispatcher(List<UpdateRouter> updateRouters,
                                  CompositeNoMatchStrategy noMatchStrategy) {
        this.updateRouters = updateRouters;
        this.noMatchStrategy = noMatchStrategy;
    }

    @Override
    public void dispatch(Update update) {
        for (UpdateRouter updateRouter : updateRouters) {
            if (updateRouter.supports(update)) {
                if (!updateRouter.route(update)) {
                    noMatchStrategy.handle(update);
                }
                return;
            }
        }
        noMatchStrategy.handle(update);
    }
}
