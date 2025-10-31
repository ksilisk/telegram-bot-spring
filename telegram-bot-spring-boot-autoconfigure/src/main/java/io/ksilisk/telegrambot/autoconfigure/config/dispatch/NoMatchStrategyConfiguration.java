package io.ksilisk.telegrambot.autoconfigure.config.dispatch;

import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.logger.BotLogger;
import io.ksilisk.telegrambot.core.selector.NoMatchStrategySelector;
import io.ksilisk.telegrambot.core.selector.impl.DefaultNoMatchStrategySelector;
import io.ksilisk.telegrambot.core.strategy.CompositeNoMatchStrategy;
import io.ksilisk.telegrambot.core.strategy.NoMatchStrategy;
import io.ksilisk.telegrambot.core.strategy.StrategyErrorPolicy;
import io.ksilisk.telegrambot.core.strategy.impl.LoggingNoMatchStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class NoMatchStrategyConfiguration {
    @Bean
    @ConditionalOnMissingBean(CompositeNoMatchStrategy.class)
    public CompositeNoMatchStrategy noMatchStrategy(List<NoMatchStrategy> noMatchStrategies,
                                                    NoMatchStrategySelector noMatchStrategySelector,
                                                    TelegramBotProperties telegramBotProperties,
                                                    BotLogger botLogger) {
        StrategyErrorPolicy errorPolicy = telegramBotProperties.getNomatch().getErrorPolicy();
        return new CompositeNoMatchStrategy(noMatchStrategies, noMatchStrategySelector, errorPolicy, botLogger);
    }

    @Bean
    @ConditionalOnMissingBean(NoMatchStrategySelector.class)
    public NoMatchStrategySelector noMatchStrategySelector() {
        return new DefaultNoMatchStrategySelector();
    }

    @Bean
    @ConditionalOnMissingBean(value = NoMatchStrategy.class, ignored = CompositeNoMatchStrategy.class)
    public NoMatchStrategy defaultLoggingNoMatchStrategy(BotLogger botLogger) {
        return new LoggingNoMatchStrategy(botLogger);
    }
}
