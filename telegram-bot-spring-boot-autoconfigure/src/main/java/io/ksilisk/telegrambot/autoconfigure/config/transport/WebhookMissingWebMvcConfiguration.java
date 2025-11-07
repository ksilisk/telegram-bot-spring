package io.ksilisk.telegrambot.autoconfigure.config.transport;

import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "WEBHOOK")
@ConditionalOnMissingClass("org.springframework.web.servlet.DispatcherServlet")
public class WebhookMissingWebMvcConfiguration {
    @Bean
    public TelegramBotExecutor telegramWebhookModeButNoWebMvc() {
        throw new IllegalStateException(
                "telegram.bot.mode=WEBHOOK, but Spring MVC is not on the classpath. " +
                        "Please add 'spring-boot-starter-web' dependency or switch telegram.bot.mode to LONG_POLLING."
        );
    }
}
