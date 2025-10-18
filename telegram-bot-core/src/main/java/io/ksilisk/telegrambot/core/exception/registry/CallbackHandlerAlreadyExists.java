package io.ksilisk.telegrambot.core.exception.registry;

public class CallbackHandlerAlreadyExists extends RuntimeException {
    public CallbackHandlerAlreadyExists(String message) {
        super(message);
    }
}
