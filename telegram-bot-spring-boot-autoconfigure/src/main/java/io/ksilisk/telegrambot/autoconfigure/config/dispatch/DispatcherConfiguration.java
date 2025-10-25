package io.ksilisk.telegrambot.autoconfigure.config.dispatch;

import io.ksilisk.telegrambot.core.dispatcher.SimpleUpdateDispatcher;
import io.ksilisk.telegrambot.core.dispatcher.UpdateDispatcher;
import io.ksilisk.telegrambot.core.handler.exception.CompositeExceptionHandler;
import io.ksilisk.telegrambot.core.router.UpdateRouter;
import io.ksilisk.telegrambot.core.strategy.CompositeNoMatchStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class DispatcherConfiguration {

    @Bean
    @ConditionalOnMissingBean(UpdateDispatcher.class)
    public UpdateDispatcher updateDispatcher(List<UpdateRouter> updateRouters,
                                             CompositeExceptionHandler compositeExceptionHandler,
                                             CompositeNoMatchStrategy compositeNoMatchStrategy) {
        return new SimpleUpdateDispatcher(updateRouters, compositeExceptionHandler, compositeNoMatchStrategy);
    }
}
