package io.ksilisk.telegrambot.autoconfigure.config.transport;

import io.ksilisk.telegrambot.core.ingress.UpdateIngress;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "LONG_POLLING", matchIfMissing = true)
@ConditionalOnMissingClass("io.ksilisk.telegrambot.longpolling.ingress.LongPollingUpdateIngress")
public class LongPollingMissingIngressConfiguration {
    @Bean
    public UpdateIngress telegramLongPollingModeButNoIngress() {
        throw new IllegalStateException(
                "telegram.bot.mode=LONG_POLLING, but Long-Polling ingress is not on the classpath. " +
                        "Please add 'telegram-bot-long-polling' dependency"
        );
    }
}
