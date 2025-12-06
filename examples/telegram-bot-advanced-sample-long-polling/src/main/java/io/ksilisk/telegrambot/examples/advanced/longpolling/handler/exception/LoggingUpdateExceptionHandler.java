package io.ksilisk.telegrambot.examples.advanced.longpolling.handler.exception;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.exception.UpdateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class LoggingUpdateExceptionHandler implements UpdateExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(LoggingUpdateExceptionHandler.class);

    @Override
    public boolean supports(Throwable t, Update update) {
        return true;
    }

    @Override
    public void handle(Throwable t, Update update) {
        log.warn("Handled exception during the handling an update. Update: {}", update, t);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
