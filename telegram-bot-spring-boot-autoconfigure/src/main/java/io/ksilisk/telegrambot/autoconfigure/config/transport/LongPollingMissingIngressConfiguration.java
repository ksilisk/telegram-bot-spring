package io.ksilisk.telegrambot.autoconfigure.config.transport;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "WEBHOOK")
@ConditionalOnMissingClass("io.ksilisk.telegrambot.longpolling.ingress.LongPollingUpdateIngress")
public class LongPollingMissingIngressConfiguration {
}
