package io.ksilisk.telegrambot.autoconfigure.config.transport;

import io.ksilisk.telegrambot.core.ingress.UpdateIngress;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "CUSTOM")
@ConditionalOnMissingBean(UpdateIngress.class)
public class CustomMissingIngressConfiguration {
    @Bean
    public UpdateIngress updateIngress() {
        throw new IllegalStateException(
                "telegram.bot.mode=CUSTOM, but ingress is not on the classpath. " +
                        "Please add ingress implementation or switch telegram.bot.mode to another one"
        );
    }
}
