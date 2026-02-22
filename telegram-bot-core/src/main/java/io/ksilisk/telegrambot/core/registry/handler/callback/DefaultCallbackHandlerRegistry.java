package io.ksilisk.telegrambot.core.registry.handler.callback;

import io.ksilisk.telegrambot.core.exception.registry.CallbackHandlerAlreadyExists;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultCallbackHandlerRegistry implements CallbackHandlerRegistry {
    private static final Logger log = LoggerFactory.getLogger(DefaultCallbackHandlerRegistry.class);

    private final Map<String, CallbackUpdateHandler> callbackUpdateHandlerMap = new HashMap<>();

    public DefaultCallbackHandlerRegistry(Collection<? extends CallbackUpdateHandler> callbackUpdateHandlers) {
        for (CallbackUpdateHandler handler : callbackUpdateHandlers) {
            log.debug("Registering callback handler '{}' with callbacks={}",
                    handler.getClass().getSimpleName(), handler.callbacks());
            for (String data : handler.callbacks()) {
                if (callbackUpdateHandlerMap.containsKey(data)) {
                    throw new CallbackHandlerAlreadyExists("Handler for callback_data='" +
                            data + "' has been already registered");
                }
                callbackUpdateHandlerMap.put(data, handler);
            }
        }
    }

    @Override
    public Optional<UpdateHandler> find(String callbackData) {
        return Optional.ofNullable(callbackUpdateHandlerMap.get(callbackData));
    }
}
