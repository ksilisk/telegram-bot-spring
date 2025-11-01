package io.ksilisk.telegrambot.core.strategy.impl;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.strategy.NoMatchStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingNoMatchStrategy implements NoMatchStrategy {
    private static final Logger log = LoggerFactory.getLogger(LoggingNoMatchStrategy.class);

    @Override
    public void handle(Update update) {
        log.warn("Handler for update not found. Update: {}", update);
    }
}
