package io.ksilisk.telegrambot.core.registry.handler.command;

import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import io.ksilisk.telegrambot.core.registry.handler.HandlerRegistry;

/**
 * Registry of command handlers keyed by command name.
 */
public interface CommandHandlerRegistry extends HandlerRegistry<CommandUpdateHandler, String> {
}
