package io.ksilisk.telegrambot.core.strategy.impl;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.strategy.UpdateNoMatchStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingUpdateNoMatchStrategy implements UpdateNoMatchStrategy {
    private static final Logger log = LoggerFactory.getLogger(LoggingUpdateNoMatchStrategy.class);

    @Override
    public void handle(Update update) {
        log.warn("Handler for update not found. Update: {}", update);
    }
}
