package io.ksilisk.telegrambot.autoconfigure.config.dispatch;

import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.handler.exception.CompositeUpdateExceptionHandler;
import io.ksilisk.telegrambot.core.handler.exception.ExceptionHandlerErrorPolicy;
import io.ksilisk.telegrambot.core.handler.exception.UpdateExceptionHandler;
import io.ksilisk.telegrambot.core.handler.exception.impl.LoggingUpdateExceptionHandler;
import io.ksilisk.telegrambot.core.selector.UpdateExceptionHandlerSelector;
import io.ksilisk.telegrambot.core.selector.impl.DefaultExceptionHandlerSelector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class ExceptionHandlerConfiguration {

    @Bean
    @ConditionalOnMissingBean(CompositeUpdateExceptionHandler.class)
    public CompositeUpdateExceptionHandler exceptionHandler(List<UpdateExceptionHandler> updateExceptionHandlers,
                                                            UpdateExceptionHandlerSelector exceptionHandlerSelector,
                                                            TelegramBotProperties telegramBotProperties) {
        ExceptionHandlerErrorPolicy errorPolicy = telegramBotProperties.getException().getErrorPolicy();
        return new CompositeUpdateExceptionHandler(updateExceptionHandlers, exceptionHandlerSelector, errorPolicy);
    }

    @Bean
    @ConditionalOnMissingBean(UpdateExceptionHandlerSelector.class)
    public UpdateExceptionHandlerSelector exceptionHandlerSelector() {
        return new DefaultExceptionHandlerSelector();
    }

    @Bean
    @ConditionalOnMissingBean(value = UpdateExceptionHandler.class, ignored = CompositeUpdateExceptionHandler.class)
    public UpdateExceptionHandler defaultLoggingExceptionHandler() {
        return new LoggingUpdateExceptionHandler();
    }
}
