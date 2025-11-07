package io.ksilisk.telegrambot.core.webhook;

public interface WebhookLifecycle {
    void register();

    void remove();
}
