package io.ksilisk.telegrambot.autoconfigure.config.transport.client;

import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.executor.resolver.DefaultSwitchableTelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.core.executor.resolver.SwitchableTelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.core.executor.resolver.TelegramBotApiUrlProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class TelegramClientCoreConfiguration {

    @Bean
    @ConditionalOnMissingBean(TelegramBotApiUrlProvider.class)
    public SwitchableTelegramBotApiUrlProvider telegramBotApiUrlProvider(TelegramBotProperties properties) {
        return new DefaultSwitchableTelegramBotApiUrlProvider(
                properties.getToken(),
                properties.getUseTestServer(),
                properties.getClient().getApi().getEndpoints()
        );
    }
}
