package io.ksilisk.telegrambot.core.exception.webhook;

public class WebhookRegisteringException extends RuntimeException {
    public WebhookRegisteringException(String message) {
        super(message);
    }

    public WebhookRegisteringException(String message, Throwable cause) {
        super(message, cause);
    }
}
