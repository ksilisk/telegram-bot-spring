package io.ksilisk.telegrambot.core.dispatcher;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.router.CompositeUpdateRouter;
import io.ksilisk.telegrambot.core.strategy.CompositeUpdateNoMatchStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleUpdateDispatcher implements UpdateDispatcher {
    private static final Logger log = LoggerFactory.getLogger(SimpleUpdateDispatcher.class);

    private final CompositeUpdateRouter compositeUpdateRouter;
    private final CompositeUpdateNoMatchStrategy noMatchStrategy;

    public SimpleUpdateDispatcher(CompositeUpdateRouter compositeUpdateRouter,
                                  CompositeUpdateNoMatchStrategy noMatchStrategy) {
        this.noMatchStrategy = noMatchStrategy;
        this.compositeUpdateRouter = compositeUpdateRouter;
    }

    @Override
    public void dispatch(Update update) {
        if (!compositeUpdateRouter.route(update)) {
            noMatchStrategy.handle(update);
            return;
        }
        log.debug("Successfully dispatched update (id={})", update.updateId());
    }
}
