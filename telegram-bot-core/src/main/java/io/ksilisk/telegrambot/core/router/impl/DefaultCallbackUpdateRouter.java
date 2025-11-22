package io.ksilisk.telegrambot.core.router.impl;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.registry.handler.callback.CallbackHandlerRegistry;
import io.ksilisk.telegrambot.core.router.CallbackUpdateRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DefaultCallbackUpdateRouter implements CallbackUpdateRouter {
    private static final Logger log = LoggerFactory.getLogger(DefaultCallbackUpdateRouter.class);

    private final CallbackHandlerRegistry callbackHandlerRegistry;

    public DefaultCallbackUpdateRouter(CallbackHandlerRegistry callbackHandlerRegistry) {
        this.callbackHandlerRegistry = callbackHandlerRegistry;
    }

    @Override
    public boolean supports(Update update) {
        return update.callbackQuery() != null;
    }

    @Override
    public boolean route(Update update) {
        if (!this.supports(update)) {
            log.warn("CallbackUpdateRouter invoked with unsupported update (missing callbackQuery). \n" +
                    "Ensure that router.supports(update) is checked before calling route().");
            return false;
        }

        Optional<UpdateHandler> updateHandler = callbackHandlerRegistry.find(update.callbackQuery().data());
        if (updateHandler.isEmpty()) {
            return false;
        }
        updateHandler.get().handle(update);
        return true;
    }
}
