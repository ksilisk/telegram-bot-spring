package io.ksilisk.telegrambot.core.registry.handler.callback;

import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.registry.handler.HandlerRegistry;

/// a registry that contains callback_query data with its handler
public interface CallbackHandlerRegistry extends HandlerRegistry<CallbackUpdateHandler, String> {
}
