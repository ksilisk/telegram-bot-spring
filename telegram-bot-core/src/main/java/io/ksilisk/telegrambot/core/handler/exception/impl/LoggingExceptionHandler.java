package io.ksilisk.telegrambot.core.handler.exception.impl;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.exception.ExceptionHandler;
import io.ksilisk.telegrambot.core.logger.BotLogger;

public class LoggingExceptionHandler implements ExceptionHandler {
    private final BotLogger botLogger;

    public LoggingExceptionHandler(BotLogger botLogger) {
        this.botLogger = botLogger;
    }

    @Override
    public boolean supports(Throwable t, Update update) {
        return true;
    }

    @Override
    public void handle(Throwable t, Update update) {
        botLogger.error("Error while handling update. Update: {}", t, update);
    }
}
