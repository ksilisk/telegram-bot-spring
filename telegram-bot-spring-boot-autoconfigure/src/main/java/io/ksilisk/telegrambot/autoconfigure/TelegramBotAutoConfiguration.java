package io.ksilisk.telegrambot.autoconfigure;

import io.ksilisk.telegrambot.autoconfigure.config.dispatch.DispatcherConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.dispatch.ExceptionHandlerConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.dispatch.NoMatchStrategyConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.dispatch.RouterConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.log.LoggingConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.registry.RegistryConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.transport.HttpClientConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.transport.LongPollingConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.transport.WebhookConfiguration;
import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties(TelegramBotProperties.class)
@Import({
        LongPollingConfiguration.class,
        WebhookConfiguration.class,
        HttpClientConfiguration.class,
        RegistryConfiguration.class,
        RouterConfiguration.class,
        NoMatchStrategyConfiguration.class,
        ExceptionHandlerConfiguration.class,
        DispatcherConfiguration.class,
        LoggingConfiguration.class
})

public class TelegramBotAutoConfiguration {
}
