package io.ksilisk.telegrambot.core.registry.handler;

import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.registry.UpdateRegistry;

/**
 * Registry for direct {@link UpdateHandler} mappings.
 *
 * <p>Used when a handler is resolved directly by a key
 * (e.g. command name or callback data), without rule matching.</p>
 *
 * @param <S> the handler type
 * @param <K> the lookup key type
 */
public interface HandlerRegistry<S extends UpdateHandler, K> extends UpdateRegistry<K> {

}
