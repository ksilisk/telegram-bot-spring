package io.ksilisk.telegrambot.core.strategy.impl;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.logger.BotLogger;
import io.ksilisk.telegrambot.core.strategy.NoMatchStrategy;

public class LoggingNoMatchStrategy implements NoMatchStrategy {
    private final BotLogger botLogger;

    public LoggingNoMatchStrategy(BotLogger botLogger) {
        this.botLogger = botLogger;
    }

    @Override
    public void handle(Update update) {
        botLogger.warn("Handler for update not found. Update: {}", update);
    }
}
