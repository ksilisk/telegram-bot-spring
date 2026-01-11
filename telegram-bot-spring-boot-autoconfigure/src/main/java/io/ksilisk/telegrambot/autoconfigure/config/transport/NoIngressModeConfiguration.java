package io.ksilisk.telegrambot.autoconfigure.config.transport;

import io.ksilisk.telegrambot.core.ingress.NoIngressUpdateIngress;
import io.ksilisk.telegrambot.core.ingress.UpdateIngress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "NO_INGRESS")
public class NoIngressModeConfiguration {
    private static final Logger log = LoggerFactory.getLogger(NoIngressModeConfiguration.class);

    @Bean
    public UpdateIngress updateIngress() {
        log.info("Telegram bot is running in NO_INGRESS mode. Incoming updates are disabled.");
        return new NoIngressUpdateIngress();
    }
}
