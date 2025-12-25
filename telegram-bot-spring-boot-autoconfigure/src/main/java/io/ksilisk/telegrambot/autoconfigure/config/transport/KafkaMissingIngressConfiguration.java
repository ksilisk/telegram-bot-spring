package io.ksilisk.telegrambot.autoconfigure.config.transport;

import io.ksilisk.telegrambot.core.ingress.UpdateIngress;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "KAFKA")
@ConditionalOnMissingClass("io.ksilisk.telegrambot.kafka.ingress.KafkaUpdateIngress")
public class KafkaMissingIngressConfiguration {
    @Bean
    public UpdateIngress telegramWebhookModeButNoIngress() {
        throw new IllegalStateException(
                "telegram.bot.mode=KAFKA, but Kafka ingress is not on the classpath. " +
                        "Please add 'telegram-bot-kafka' dependency or switch telegram.bot.mode to LONG_POLLING."
        );
    }
}
