package io.ksilisk.telegrambot.autoconfigure.config.dispatch;

import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.handler.exception.CompositeExceptionHandler;
import io.ksilisk.telegrambot.core.handler.exception.ExceptionHandler;
import io.ksilisk.telegrambot.core.handler.exception.ExceptionHandlerErrorPolicy;
import io.ksilisk.telegrambot.core.handler.exception.impl.LoggingExceptionHandler;
import io.ksilisk.telegrambot.core.logger.BotLogger;
import io.ksilisk.telegrambot.core.selector.ExceptionHandlerSelector;
import io.ksilisk.telegrambot.core.selector.impl.DefaultExceptionHandlerSelector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class ExceptionHandlerConfiguration {

    @Bean
    @ConditionalOnMissingBean(CompositeExceptionHandler.class)
    public CompositeExceptionHandler exceptionHandler(List<ExceptionHandler> exceptionHandlers,
                                                      ExceptionHandlerSelector exceptionHandlerSelector,
                                                      TelegramBotProperties telegramBotProperties,
                                                      BotLogger botLogger) {
        ExceptionHandlerErrorPolicy errorPolicy = telegramBotProperties.getException().getErrorPolicy();
        return new CompositeExceptionHandler(exceptionHandlers, exceptionHandlerSelector, errorPolicy, botLogger);
    }

    @Bean
    @ConditionalOnMissingBean(ExceptionHandlerSelector.class)
    public ExceptionHandlerSelector exceptionHandlerSelector() {
        return new DefaultExceptionHandlerSelector();
    }

    @Bean
    @ConditionalOnMissingBean(value = ExceptionHandler.class, ignored = CompositeExceptionHandler.class)
    public ExceptionHandler defaultLoggingExceptionHandler(BotLogger botLogger) {
        return new LoggingExceptionHandler(botLogger);
    }
}
