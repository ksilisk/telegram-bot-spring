package io.ksilisk.telegrambot.core.handler.update;

import com.pengrad.telegrambot.model.Update;

public interface UpdateHandler {
    void handle(Update update);
}
