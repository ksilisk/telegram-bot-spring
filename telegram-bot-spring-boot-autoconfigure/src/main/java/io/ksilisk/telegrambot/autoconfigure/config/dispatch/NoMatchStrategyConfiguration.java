package io.ksilisk.telegrambot.autoconfigure.config.dispatch;

import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.selector.UpdateNoMatchStrategySelector;
import io.ksilisk.telegrambot.core.selector.impl.DefaultNoMatchStrategySelector;
import io.ksilisk.telegrambot.core.strategy.CompositeUpdateNoMatchStrategy;
import io.ksilisk.telegrambot.core.strategy.StrategyErrorPolicy;
import io.ksilisk.telegrambot.core.strategy.UpdateNoMatchStrategy;
import io.ksilisk.telegrambot.core.strategy.impl.LoggingUpdateNoMatchStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class NoMatchStrategyConfiguration {
    @Bean
    @ConditionalOnMissingBean(CompositeUpdateNoMatchStrategy.class)
    public CompositeUpdateNoMatchStrategy noMatchStrategy(List<UpdateNoMatchStrategy> noMatchStrategies,
                                                          UpdateNoMatchStrategySelector noMatchStrategySelector,
                                                          TelegramBotProperties telegramBotProperties) {
        StrategyErrorPolicy errorPolicy = telegramBotProperties.getNomatch().getErrorPolicy();
        return new CompositeUpdateNoMatchStrategy(noMatchStrategies, noMatchStrategySelector, errorPolicy);
    }

    @Bean
    @ConditionalOnMissingBean(UpdateNoMatchStrategySelector.class)
    public UpdateNoMatchStrategySelector noMatchStrategySelector() {
        return new DefaultNoMatchStrategySelector();
    }

    @Bean
    @ConditionalOnMissingBean(value = UpdateNoMatchStrategy.class, ignored = CompositeUpdateNoMatchStrategy.class)
    public UpdateNoMatchStrategy defaultLoggingNoMatchStrategy() {
        return new LoggingUpdateNoMatchStrategy();
    }
}
