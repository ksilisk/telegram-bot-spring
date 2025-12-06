package io.ksilisk.telegrambot.autoconfigure;

import com.pengrad.telegrambot.TelegramBot;
import io.ksilisk.telegrambot.autoconfigure.config.dispatch.DeliveryConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.dispatch.DispatcherConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.dispatch.ExceptionHandlerConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.dispatch.NoMatchStrategyConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.dispatch.RoutingConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.transport.LongPollingMissingIngressConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.transport.TelegramClientConfiguration;
import io.ksilisk.telegrambot.autoconfigure.config.transport.WebhookMissingIngressConfiguration;
import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties(TelegramBotProperties.class)
@ConditionalOnClass(TelegramBot.class)
@Import({
        LongPollingMissingIngressConfiguration.class,
        WebhookMissingIngressConfiguration.class,
        TelegramClientConfiguration.class,
        NoMatchStrategyConfiguration.class,
        ExceptionHandlerConfiguration.class,
        RoutingConfiguration.class,
        DispatcherConfiguration.class,
        DeliveryConfiguration.class
})
public class TelegramBotAutoConfiguration {
}
