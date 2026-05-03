package io.ksilisk.telegrambot.core.executor;

import io.ksilisk.telegrambot.core.file.TelegramBotFileClient;

/**
 * Factory for creating Telegram Bot API clients for the currently configured endpoint provider.
 */
public interface TelegramBotClientFactory {
    /**
     * Creates a Telegram Bot API request executor.
     *
     * @return executor instance
     */
    TelegramBotExecutor createExecutor();

    /**
     * Creates a Telegram Bot API file client.
     *
     * @param telegramBotExecutor executor used for Bot API metadata requests, such as file path resolution
     * @return file client instance
     */
    TelegramBotFileClient createFileClient(TelegramBotExecutor telegramBotExecutor);
}
