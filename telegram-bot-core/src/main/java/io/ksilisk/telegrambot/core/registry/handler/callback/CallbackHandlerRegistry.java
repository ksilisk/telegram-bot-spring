package io.ksilisk.telegrambot.core.registry.handler.callback;

import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.registry.handler.HandlerRegistry;

/**
 * Registry of callback query handlers keyed by callback data.
 */
public interface CallbackHandlerRegistry extends HandlerRegistry<CallbackUpdateHandler, String> {
}
