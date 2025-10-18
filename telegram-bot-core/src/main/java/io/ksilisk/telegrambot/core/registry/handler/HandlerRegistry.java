package io.ksilisk.telegrambot.core.registry.handler;

import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.registry.Registry;

/// used for types of handlers that don't need a rule (just a string to determine a handler)
public interface HandlerRegistry<S extends UpdateHandler, K> extends Registry<S, K> {

}
