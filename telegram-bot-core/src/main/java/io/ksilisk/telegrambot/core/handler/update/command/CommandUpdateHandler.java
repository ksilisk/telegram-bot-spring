package io.ksilisk.telegrambot.core.handler.update.command;

import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;

import java.util.Set;

/**
 * {@link UpdateHandler} for command-based message updates.
 *
 * <p>Handlers are selected based on the commands returned from
 * {@link #commands()} (for example {@code "/start"}, {@code "/help"}).</p>
 */
public interface CommandUpdateHandler extends UpdateHandler {
    /**
     * Commands supported by this handler.
     *
     * @return a non-empty set of command names
     */
    Set<String> commands();
}
