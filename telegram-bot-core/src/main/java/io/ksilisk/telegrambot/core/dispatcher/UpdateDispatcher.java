package io.ksilisk.telegrambot.core.dispatcher;

import com.pengrad.telegrambot.model.Update;

public interface UpdateDispatcher {
    void dispatch(Update update);
}
