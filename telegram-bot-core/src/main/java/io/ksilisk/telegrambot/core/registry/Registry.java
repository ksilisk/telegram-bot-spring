package io.ksilisk.telegrambot.core.registry;

import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;

import java.util.Optional;

/**
 * Generic registry for routing components.
 *
 * <p>Registries store objects used during update processing and provide
 * lookup methods to resolve an appropriate {@link UpdateHandler} for
 * a given key or update payload.</p>
 *
 * @param <S> the stored object type (handler, rule, etc.)
 * @param <K> the lookup key or update payload type
 */
public interface Registry<S, K> {
    void register(S object);

    Optional<UpdateHandler> find(K update);
}
