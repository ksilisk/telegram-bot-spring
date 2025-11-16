package io.ksilisk.telegrambot.autoconfigure.config.dispatch;

import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.delivery.DefaultDeliveryThreadPoolExecutorFactory;
import io.ksilisk.telegrambot.core.delivery.DefaultUpdateDelivery;
import io.ksilisk.telegrambot.core.delivery.DeliveryThreadPoolExecutorFactory;
import io.ksilisk.telegrambot.core.delivery.UpdateDelivery;
import io.ksilisk.telegrambot.core.dispatcher.UpdateDispatcher;
import io.ksilisk.telegrambot.core.handler.exception.CompositeExceptionHandler;
import io.ksilisk.telegrambot.core.interceptor.CompositeUpdateInterceptor;
import io.ksilisk.telegrambot.core.interceptor.UpdateInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class DeliveryConfiguration {
    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(UpdateDelivery.class)
    public UpdateDelivery updateDelivery(UpdateDispatcher updateDispatcher,
                                         DeliveryThreadPoolExecutorFactory threadPoolExecutorFactory,
                                         TelegramBotProperties telegramBotProperties,
                                         CompositeUpdateInterceptor compositeUpdateInterceptor,
                                         CompositeExceptionHandler compositeExceptionHandler) {
        return new DefaultUpdateDelivery(updateDispatcher,
                threadPoolExecutorFactory.buildThreadPoolExecutor(),
                telegramBotProperties.getDelivery(),
                compositeUpdateInterceptor,
                compositeExceptionHandler);
    }

    @Bean
    @ConditionalOnMissingBean(DeliveryThreadPoolExecutorFactory.class)
    public DeliveryThreadPoolExecutorFactory deliveryThreadPoolExecutorFactory(TelegramBotProperties telegramBotProperties) {
        return new DefaultDeliveryThreadPoolExecutorFactory(telegramBotProperties.getDelivery());
    }

    @Bean
    @ConditionalOnMissingBean(CompositeUpdateInterceptor.class)
    public CompositeUpdateInterceptor compositeUpdateInterceptor(List<UpdateInterceptor> updateInterceptorList) {
        return new CompositeUpdateInterceptor(updateInterceptorList);
    }
}
