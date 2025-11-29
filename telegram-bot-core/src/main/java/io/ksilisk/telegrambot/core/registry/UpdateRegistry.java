package io.ksilisk.telegrambot.core.registry;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;

import java.util.Optional;

/**
 * see {@link Registry}
 */
public interface UpdateRegistry<K> extends Registry<K, UpdateHandler, Update> {
    Optional<UpdateHandler> find(K update);
}
