package io.ksilisk.telegrambot.core.processor;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.dispatcher.UpdateDispatcher;

import java.util.List;

public class DefaultTelegramBotUpdatesProcessor implements TelegramBotUpdatesProcessor {
    private final UpdateDispatcher updateDispatcher;

    public DefaultTelegramBotUpdatesProcessor(UpdateDispatcher updateDispatcher) {
        this.updateDispatcher = updateDispatcher;
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            updateDispatcher.dispatch(update);
        }
        return CONFIRMED_UPDATES_ALL;
    }
}
