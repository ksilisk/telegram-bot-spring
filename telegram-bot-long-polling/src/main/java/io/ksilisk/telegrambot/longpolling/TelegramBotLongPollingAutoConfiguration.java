package io.ksilisk.telegrambot.longpolling;

import io.ksilisk.telegrambot.longpolling.configuration.LongPollingIngressConfiguration;
import io.ksilisk.telegrambot.longpolling.configuration.LongPollingMasterConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "LONG_POLLING", matchIfMissing = true)
@Import({
        LongPollingMasterConfiguration.class,
        LongPollingIngressConfiguration.class,
})
public class TelegramBotLongPollingAutoConfiguration {
}
