package io.ksilisk.telegrambot.autoconfigure.config.transport;

import com.pengrad.telegrambot.TelegramBot;
import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.dispatcher.UpdateDispatcher;
import io.ksilisk.telegrambot.core.executor.SimpleTelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.processor.DefaultTelegramBotUpdatesProcessor;
import io.ksilisk.telegrambot.core.processor.TelegramBotUpdatesProcessor;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "LONG_POLLING", matchIfMissing = true)
public class LongPollingConfiguration {
    @Bean
    @ConditionalOnMissingBean(TelegramBot.class)
    public TelegramBot telegramBot(TelegramBotProperties telegramBotProperties,
                                   TelegramBotUpdatesProcessor updatesProcessor,
                                   OkHttpClient okHttpClient) {
        long delayMillis = telegramBotProperties.getLongPolling().getDelay().toMillis();

        TelegramBot.Builder builder = new TelegramBot.Builder(telegramBotProperties.getToken());
        builder.useTestServer(telegramBotProperties.getUseTestServer());
        builder.updateListenerSleep(delayMillis);
        builder.okHttpClient(okHttpClient);

        TelegramBot telegramBot = builder.build();
        telegramBot.setUpdatesListener(updatesProcessor);

        return telegramBot;
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotExecutor.class)
    public TelegramBotExecutor telegramBotExecutor(TelegramBot telegramBot) {
        return new SimpleTelegramBotExecutor(telegramBot);
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotUpdatesProcessor.class)
    public TelegramBotUpdatesProcessor telegramBotUpdatesProcessor(UpdateDispatcher updateDispatcher) {
        return new DefaultTelegramBotUpdatesProcessor(updateDispatcher);
    }
}
