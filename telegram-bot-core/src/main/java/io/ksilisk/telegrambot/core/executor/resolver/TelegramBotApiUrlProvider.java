package io.ksilisk.telegrambot.core.executor.resolver;

/**
 * Provides the base URL of the Telegram Bot API endpoint.
 *
 * <p>Implementations may resolve the URL from configuration, environment,
 * or custom logic. The returned value must be a valid absolute URL and
 * must not contain a trailing slash.</p>
 */
public interface TelegramBotApiUrlProvider {
    /**
     * Return the base Telegram Bot API URL.
     *
     * @return a non-empty absolute URL, never {@code null}
     */
    String getApiUrl();
}
