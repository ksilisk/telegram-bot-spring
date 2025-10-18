package io.ksilisk.telegrambot.core.registry.handler.command;

import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import io.ksilisk.telegrambot.core.registry.handler.HandlerRegistry;

/// a registry that contains commands with its handler
public interface CommandHandlerRegistry extends HandlerRegistry<CommandUpdateHandler, String> {
}
