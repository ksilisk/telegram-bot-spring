package io.ksilisk.telegrambot.autoconfigure.config.dispatch;

import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.delivery.DefaultDeliveryThreadPoolExecutorFactory;
import io.ksilisk.telegrambot.core.delivery.DefaultUpdateDelivery;
import io.ksilisk.telegrambot.core.delivery.DeliveryThreadPoolExecutorFactory;
import io.ksilisk.telegrambot.core.delivery.UpdateDelivery;
import io.ksilisk.telegrambot.core.dispatcher.UpdateDispatcher;
import io.ksilisk.telegrambot.core.handler.exception.CompositeUpdateExceptionHandler;
import io.ksilisk.telegrambot.core.interceptor.CompositeUpdateInterceptor;
import io.ksilisk.telegrambot.core.interceptor.UpdateInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;

@Configuration(proxyBeanMethods = false)
public class DeliveryConfiguration {
    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean(UpdateDelivery.class)
    public UpdateDelivery updateDelivery(UpdateDispatcher updateDispatcher,
                                         DeliveryThreadPoolExecutorFactory threadPoolExecutorFactory,
                                         TelegramBotProperties telegramBotProperties,
                                         CompositeUpdateInterceptor compositeUpdateInterceptor,
                                         CompositeUpdateExceptionHandler compositeExceptionHandler) {
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
    @ConditionalOnMissingBean(DeliveryThreadPoolExecutorFactory.class)
    @ConditionalOnBean(RejectedExecutionException.class)
    public DeliveryThreadPoolExecutorFactory deliveryThreadPoolExecutorFactoryWithRejectionHandler(
            TelegramBotProperties telegramBotProperties, RejectedExecutionHandler rejectedExecutionHandler) {
        return new DefaultDeliveryThreadPoolExecutorFactory(telegramBotProperties.getDelivery(), rejectedExecutionHandler);
    }

    @Bean
    @ConditionalOnMissingBean(CompositeUpdateInterceptor.class)
    public CompositeUpdateInterceptor compositeUpdateInterceptor(List<UpdateInterceptor> updateInterceptorList) {
        return new CompositeUpdateInterceptor(updateInterceptorList);
    }
}
