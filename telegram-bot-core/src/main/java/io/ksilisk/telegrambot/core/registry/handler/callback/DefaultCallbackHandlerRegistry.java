package io.ksilisk.telegrambot.core.registry.handler.callback;

import io.ksilisk.telegrambot.core.exception.registry.CallbackHandlerAlreadyExists;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultCallbackHandlerRegistry implements CallbackHandlerRegistry {
    private final Map<String, CallbackUpdateHandler> callbackUpdateHandlerMap = new HashMap<>();

    @Override
    public void register(CallbackUpdateHandler handler) {
        for (String data : handler.callbacks()) {
            if (callbackUpdateHandlerMap.containsKey(data)) {
                throw new CallbackHandlerAlreadyExists("Handler for callback_data='" + data + "' has been already registered");
            }
            callbackUpdateHandlerMap.put(data, handler);
        }
    }

    @Override
    public Optional<UpdateHandler> find(String callbackData) {
        return Optional.ofNullable(callbackUpdateHandlerMap.get(callbackData));
    }
}
