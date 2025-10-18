package io.ksilisk.telegrambot.core.router;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.registry.handler.callback.CallbackHandlerRegistry;

import java.util.Optional;

public class CallbackUpdateRouter implements UpdateRouter {
    private final CallbackHandlerRegistry callbackHandlerRegistry;

    public CallbackUpdateRouter(CallbackHandlerRegistry callbackHandlerRegistry) {
        this.callbackHandlerRegistry = callbackHandlerRegistry;
    }

    @Override
    public boolean supports(Update update) {
        return update.callbackQuery() != null;
    }

    @Override
    public boolean route(Update update) {
        Optional<UpdateHandler> updateHandler = callbackHandlerRegistry.find(update.callbackQuery().data());
        if (updateHandler.isEmpty()) {
            return false;
        }
        updateHandler.get().handle(update);
        return true;
    }
}
