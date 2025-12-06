package io.ksilisk.telegrambot.examples.advanced.longpolling.config;

import io.ksilisk.telegrambot.core.selector.UpdateExceptionHandlerSelector;
import io.ksilisk.telegrambot.core.selector.UpdateNoMatchStrategySelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
