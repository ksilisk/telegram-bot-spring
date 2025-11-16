package io.ksilisk.telegrambot.core.webhook;

/**
 * Entry point for receiving Telegram updates via the webhook mechanism.
 *
 * <p>Framework implementations expose this controller as an HTTP endpoint
 * that accepts incoming update payloads from Telegram and forwards them
 * to the update processing pipeline.</p>
 *
 * <p>This component is only used in webhook mode; it is not active when
 * long-polling is enabled.</p>
 */
public interface WebhookController {
}
