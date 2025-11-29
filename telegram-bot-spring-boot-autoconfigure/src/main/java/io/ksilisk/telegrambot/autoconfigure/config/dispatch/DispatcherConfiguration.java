package io.ksilisk.telegrambot.autoconfigure.config.dispatch;

import io.ksilisk.telegrambot.core.dispatcher.SimpleUpdateDispatcher;
import io.ksilisk.telegrambot.core.dispatcher.UpdateDispatcher;
import io.ksilisk.telegrambot.core.router.CompositeUpdateRouter;
import io.ksilisk.telegrambot.core.strategy.CompositeUpdateNoMatchStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class DispatcherConfiguration {

    @Bean
    @ConditionalOnMissingBean(UpdateDispatcher.class)
    public UpdateDispatcher updateDispatcher(CompositeUpdateRouter compositeUpdateRouter,
                                             CompositeUpdateNoMatchStrategy compositeNoMatchStrategy) {
        return new SimpleUpdateDispatcher(compositeUpdateRouter, compositeNoMatchStrategy);
    }
}
