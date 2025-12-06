package io.ksilisk.telegrambot.examples.advanced.longpolling.nomatch;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.strategy.UpdateNoMatchStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Component
public class LoggingUpdateNoMatchStrategy implements UpdateNoMatchStrategy {
    private static final Logger log = LoggerFactory.getLogger(LoggingUpdateNoMatchStrategy.class);

    @Override
    public void handle(Update update) {
        log.warn("No handler is found for update: {}", update);
    }
}
