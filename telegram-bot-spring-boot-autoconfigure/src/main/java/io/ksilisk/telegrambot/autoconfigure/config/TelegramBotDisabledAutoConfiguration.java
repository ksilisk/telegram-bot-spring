package io.ksilisk.telegrambot.autoconfigure.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import io.ksilisk.telegrambot.core.ingress.DisabledUpdateIngress;
import io.ksilisk.telegrambot.core.ingress.UpdateIngress;

@AutoConfiguration
// This tells Spring: "Only run this if telegram.bot.mode is set to DISABLED"
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "DISABLED")
public class TelegramBotDisabledAutoConfiguration {

    @Bean
    public UpdateIngress updateIngress() {
        return new DisabledUpdateIngress();
    }
}
