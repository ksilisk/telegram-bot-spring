package io.ksilisk.telegrambot.core.exception.registry;

public class CommandHandlerAlreadyExists extends RuntimeException {
    public CommandHandlerAlreadyExists(String message) {
        super(message);
    }
}
