package io.ksilisk.telegrambot.webhook.exception;

public class WebhookRegisteringException extends RuntimeException {
    public WebhookRegisteringException(String message) {
        super(message);
    }

    public WebhookRegisteringException(String message, Throwable cause) {
        super(message, cause);
    }
}
