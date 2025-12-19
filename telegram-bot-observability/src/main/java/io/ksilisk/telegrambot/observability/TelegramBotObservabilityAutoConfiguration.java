package io.ksilisk.telegrambot.observability;

import io.ksilisk.telegrambot.observability.configuration.TelegramBotObservabilityBeanPostProcessorConfiguration;
import io.ksilisk.telegrambot.observability.configuration.TelegramBotObservabilityMetricsConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Import;

@Import({
        TelegramBotObservabilityBeanPostProcessorConfiguration.class,
        TelegramBotObservabilityMetricsConfiguration.class
})
@AutoConfiguration
@AutoConfigureAfter(name = "org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration")
public class TelegramBotObservabilityAutoConfiguration {
}
