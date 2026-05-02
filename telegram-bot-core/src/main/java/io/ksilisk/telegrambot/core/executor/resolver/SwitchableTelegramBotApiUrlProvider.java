package io.ksilisk.telegrambot.core.executor.resolver;

/**
 * {@link TelegramBotApiUrlProvider} that can switch the active Telegram Bot API endpoint at runtime.
 */
public interface SwitchableTelegramBotApiUrlProvider extends TelegramBotApiUrlProvider {
     /**
     * Switches the active endpoint to the next configured endpoint.
     */
    void switchToNext();
}
