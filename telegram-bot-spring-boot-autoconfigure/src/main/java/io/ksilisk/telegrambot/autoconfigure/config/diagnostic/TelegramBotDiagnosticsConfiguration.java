package io.ksilisk.telegrambot.autoconfigure.config.diagnostic;

import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class TelegramBotDiagnosticsConfiguration {

    @Bean
    ApplicationListener<ApplicationReadyEvent> telegramBotStartupLogger(
            ConfigurableApplicationContext ctx,
            TelegramBotProperties props
    ) {
        return event -> TelegramBotStartupDiagnostics.log(ctx, props);
    }
}
