package io.ksilisk.telegrambot.core.handler.exception;

import com.pengrad.telegrambot.model.Update;

public interface ExceptionHandler {
    boolean supports(Throwable t, Update update);

    void handle(Throwable t, Update update);

    default String name() {
        return getClass().getSimpleName();
    }

    default boolean terminal() {
        return false;
    }
}
