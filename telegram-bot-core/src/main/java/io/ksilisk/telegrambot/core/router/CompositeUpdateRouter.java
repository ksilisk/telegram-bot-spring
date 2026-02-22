package io.ksilisk.telegrambot.core.router;

import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CompositeUpdateRouter implements UpdateRouter {
    private static final Logger log = LoggerFactory.getLogger(CompositeUpdateRouter.class);

    private final List<UpdateRouter> delegates;

    public CompositeUpdateRouter(List<UpdateRouter> updateRouters) {
        this.delegates = new ArrayList<>(updateRouters);
    }

    @Override
    public boolean supports(Update update) {
        return true; // composite supports any update
    }

    @Override
    public boolean route(Update update) {
        for (UpdateRouter updateRouter : delegates) {
            if (updateRouter.supports(update)) {
                log.debug("Router '{}' matched for update (id={})", updateRouter.getClass().getSimpleName(), update.updateId());
                return updateRouter.route(update);
            }
        }
        return false;
    }
}
