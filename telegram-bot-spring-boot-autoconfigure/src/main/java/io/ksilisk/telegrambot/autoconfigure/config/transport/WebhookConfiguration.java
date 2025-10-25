package io.ksilisk.telegrambot.autoconfigure.config.transport;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "WEBHOOK")
public class WebhookConfiguration {
    // TODO implement the Webhook mode (https://github.com/ksilisk/telegram-bot-spring/issues/13)
}
