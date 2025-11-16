package io.ksilisk.telegrambot.core.exception.request;

public class TelegramRequestException extends RuntimeException {
    public TelegramRequestException(String message) {
        super(message);
    }
}
