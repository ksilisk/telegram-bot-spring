package io.ksilisk.telegrambot.core.exception.webhook;

public class WebhookRemovingException extends RuntimeException {
    public WebhookRemovingException(String message) {
        super(message);
    }
}
