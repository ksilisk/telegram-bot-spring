package io.ksilisk.telegrambot.core.registry;

import io.ksilisk.telegrambot.core.handler.update.Handler;

import java.util.Optional;

/**
 * Generic registry for routing components.
 *
 * <p>Registries store objects used during update processing and provide
 * lookup methods to resolve an appropriate {@link Handler} for
 * a given key or update payload.</p>
 *
 * @param <H> the type of handlers
 * @param <K> the lookup key or update payload type
 * @param <U> the type of value for handlers
 */
public interface Registry<K, H extends Handler<U>, U> {
    Optional<H> find(K key);
}
