package io.ksilisk.telegrambot.core.router;

import com.pengrad.telegrambot.model.Update;

import java.util.ArrayList;
import java.util.List;

public class CompositeUpdateRouter implements UpdateRouter {
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
                return updateRouter.route(update);
            }
        }
        return false;
    }
}
