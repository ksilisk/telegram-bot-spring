package io.ksilisk.telegrambot.autoconfigure.config.transport;

import io.ksilisk.telegrambot.core.ingress.UpdateIngress;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "WEBHOOK")
@ConditionalOnMissingClass("io.ksilisk.telegrambot.webhook.ingress.WebhookUpdateIngress")
public class WebhookMissingIngressConfiguration {
    @Bean
    public UpdateIngress telegramWebhookModeButNoIngress() {
        throw new IllegalStateException(
                "telegram.bot.mode=WEBHOOK, but Webhook ingress is not on the classpath. " +
                        "Please add 'telegram-bot-webhook' dependency or switch telegram.bot.mode to LONG_POLLING."
        );
    }
}
