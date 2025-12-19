package io.ksilisk.telegrambot.observability.resolver;

import io.ksilisk.telegrambot.observability.metrics.TelegramBotChannel;

/**
 * Resolves a {@link TelegramBotChannel} for an update handler.
 *
 * <p>The channel is used for observability and can be customized by providing
 * a custom resolver implementation.</p>
 */
public interface TelegramBotChannelResolver {
    /**
     * Resolve the channel for the given update handler.
     *
     * @param updateHandler the update handler instance
     * @return the resolved {@link TelegramBotChannel}, never {@code null}
     */
    TelegramBotChannel resolve(Object updateHandler);
}
