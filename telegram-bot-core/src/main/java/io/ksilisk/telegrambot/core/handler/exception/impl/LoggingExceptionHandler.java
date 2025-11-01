package io.ksilisk.telegrambot.core.handler.exception.impl;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.exception.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingExceptionHandler implements ExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(LoggingExceptionHandler.class);

    @Override
    public boolean supports(Throwable t, Update update) {
        return true;
    }

    @Override
    public void handle(Throwable t, Update update) {
        log.error("Error while handling update. Update: {}", update, t);
    }
}
