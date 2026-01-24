package io.ksilisk.telegrambot.examples.advanced.longpolling.config;

import io.ksilisk.telegrambot.core.handler.exception.impl.LoggingUpdateExceptionHandler;
import io.ksilisk.telegrambot.core.selector.UpdateExceptionHandlerSelector;
import io.ksilisk.telegrambot.core.selector.UpdateNoMatchStrategySelector;
import io.ksilisk.telegrambot.core.strategy.impl.LoggingUpdateNoMatchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
public class TelegramBotConfig {
    @Bean // to take only the first 2 exceptionHandlers
    public UpdateExceptionHandlerSelector updateExceptionHandlerSelector() {
        return (exceptionHandlers, t, update) -> exceptionHandlers.subList(0, 2);
    }

    @Bean // to take only the first 2 noMatchStrategies
    public UpdateNoMatchStrategySelector updateNoMatchStrategySelector() {
        return (noMatchStrategies, update) -> noMatchStrategies.subList(0, 2);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public LoggingUpdateNoMatchStrategy loggingUpdateNoMatchStrategy() {
        return new LoggingUpdateNoMatchStrategy();
    }

    @Bean
    public LoggingUpdateExceptionHandler loggingUpdateExceptionHandler() {
        return new LoggingUpdateExceptionHandler();
    }
}
