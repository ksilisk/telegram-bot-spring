package io.ksilisk.telegrambot.core.registry;

import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;

import java.util.Optional;

public interface Registry<S, K> {
    void register(S object);

    Optional<UpdateHandler> find(K update);
}
