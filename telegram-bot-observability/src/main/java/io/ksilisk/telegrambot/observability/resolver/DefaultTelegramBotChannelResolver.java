package io.ksilisk.telegrambot.observability.resolver;

import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import io.ksilisk.telegrambot.core.handler.update.inline.InlineUpdateHandler;
import io.ksilisk.telegrambot.core.handler.update.message.MessageUpdateHandler;
import io.ksilisk.telegrambot.observability.metrics.TelegramBotChannel;

public class DefaultTelegramBotChannelResolver implements TelegramBotChannelResolver {
    @Override
    public TelegramBotChannel resolve(Object updateHandler) {
        if (updateHandler instanceof CommandUpdateHandler) {
            return TelegramBotChannel.COMMAND;
        }
        if (updateHandler instanceof CallbackUpdateHandler) {
            return TelegramBotChannel.CALLBACK;
        }
        if (updateHandler instanceof MessageUpdateHandler) {
            return TelegramBotChannel.MESSAGE;
        }
        if (updateHandler instanceof InlineUpdateHandler) {
            return TelegramBotChannel.INLINE;
        }

        return TelegramBotChannel.OTHER;
    }
}
