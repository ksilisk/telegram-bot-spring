package io.ksilisk.telegrambot.webhook.lifecycle;

/**
 * Manages the Telegram webhook lifecycle.
 *
 * <p>Implementations typically call the Telegram Bot API to register the
 * webhook URL when the application starts, and remove it when the
 * application shuts down.</p>
 *
 * <p>Used only in webhook mode; has no effect in long-polling mode.</p>
 */
public interface WebhookLifecycle {
    /**
     * Register the webhook endpoint with the Telegram Bot API.
     */
    void register();

    /**
     * Remove the webhook registration from the Telegram Bot API.
     */
    void remove();
}
