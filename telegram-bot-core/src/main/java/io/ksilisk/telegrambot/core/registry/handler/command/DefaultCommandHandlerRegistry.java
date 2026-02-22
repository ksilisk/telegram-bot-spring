package io.ksilisk.telegrambot.core.registry.handler.command;

import io.ksilisk.telegrambot.core.exception.registry.CommandHandlerAlreadyExists;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultCommandHandlerRegistry implements CommandHandlerRegistry {
    private static final Logger log = LoggerFactory.getLogger(DefaultCommandHandlerRegistry.class);

    private final Map<String, CommandUpdateHandler> commandUpdateHandlerMap = new HashMap<>();

    public DefaultCommandHandlerRegistry(Collection<? extends CommandUpdateHandler> commandUpdateHandlers) {
        for (CommandUpdateHandler handler : commandUpdateHandlers) {
            log.debug("Registering callback handler '{}' with commands={}",
                    handler.getClass().getSimpleName(), handler.commands());
            for (String command : handler.commands()) {
                if (commandUpdateHandlerMap.containsKey(command)) {
                    throw new CommandHandlerAlreadyExists("Handler for command '" + command + "' has been already registered");
                }
                commandUpdateHandlerMap.put(command, handler);
            }
        }
    }

    @Override
    public Optional<UpdateHandler> find(String command) {
        return Optional.ofNullable(commandUpdateHandlerMap.get(command));
    }
}
