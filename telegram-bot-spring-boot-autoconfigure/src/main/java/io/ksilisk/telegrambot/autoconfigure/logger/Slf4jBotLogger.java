package io.ksilisk.telegrambot.autoconfigure.logger;

import io.ksilisk.telegrambot.core.logger.BotLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jBotLogger implements BotLogger {
    private final Logger log;

    public Slf4jBotLogger() {
        this.log = LoggerFactory.getLogger(getClass());
    }

    @Override
    public void info(String msg, Object... args) {
        log.info(msg, args);
    }

    @Override
    public void warn(String msg, Object... args) {
        log.warn(msg, args);
    }

    @Override
    public void error(String msg, Throwable ex, Object... args) {
        log.warn(msg, args, ex);
    }
}
