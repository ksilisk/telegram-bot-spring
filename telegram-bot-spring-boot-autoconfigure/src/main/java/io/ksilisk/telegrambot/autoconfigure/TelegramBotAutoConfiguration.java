package io.ksilisk.telegrambot.autoconfigure;

import com.pengrad.telegrambot.TelegramBot;
import io.ksilisk.telegrambot.autoconfigure.config.dispatch.DeliveryConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.dispatch.DispatcherConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.dispatch.ExceptionHandlerConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.dispatch.NoMatchStrategyConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.dispatch.RouterConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.registry.RegistryConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.transport.TelegramClientConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.transport.LongPollingConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.transport.WebhookConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.transport.WebhookMissingWebMvcConfiguration;
import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties(TelegramBotProperties.class)
@ConditionalOnClass(TelegramBot.class)
@Import({
        TelegramClientConfiguration.class,
        DeliveryConfiguration.class,
        RouterConfiguration.class,
        RegistryConfiguration.class,
        NoMatchStrategyConfiguration.class,
        ExceptionHandlerConfiguration.class,
        DispatcherConfiguration.class,
        LongPollingConfiguration.class,
        WebhookConfiguration.class,
        WebhookMissingWebMvcConfiguration.class
})
public class TelegramBotAutoConfiguration {
}
