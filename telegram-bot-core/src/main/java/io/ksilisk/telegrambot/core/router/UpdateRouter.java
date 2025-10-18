package io.ksilisk.telegrambot.core.router;

import com.pengrad.telegrambot.model.Update;

public interface UpdateRouter {
    boolean supports(Update update);

    boolean route(Update update);
}
