package io.ksilisk.telegrambot.core.exception.request;

public class RequestFailedException extends RuntimeException{
    public RequestFailedException(String message) {
        super(message);
    }
}
