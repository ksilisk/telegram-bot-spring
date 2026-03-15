package io.ksilisk.telegrambot.autoconfigure.config.transport.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        TelegramClientCoreConfiguration.class,
        TelegramClientRetryConfiguration.class,
        OkHttpTelegramClientConfiguration.class,
        SpringTelegramClientConfiguration.class
})
public class TelegramClientConfiguration {
}
