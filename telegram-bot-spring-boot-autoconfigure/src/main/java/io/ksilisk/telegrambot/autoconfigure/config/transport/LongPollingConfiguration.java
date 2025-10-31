package io.ksilisk.telegrambot.autoconfigure.config.transport;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.impl.TelegramBotClient;
import com.pengrad.telegrambot.utility.BotUtils;
import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.dispatcher.UpdateDispatcher;
import io.ksilisk.telegrambot.core.executor.SimpleTelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.resolver.DefaultTelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.core.executor.resolver.TelegramBotApiUrlProvider;
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
    public TelegramBotExecutor telegramBotExecutor(TelegramBotClient telegramBotClient) {
        return new SimpleTelegramBotExecutor(telegramBotClient);
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotClient.class)
    public TelegramBotClient telegramBotClient(OkHttpClient okHttpClient, TelegramBotApiUrlProvider apiUrlProvider) {
        return new TelegramBotClient(okHttpClient, BotUtils.GSON, apiUrlProvider.getApiUrl());
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotUpdatesProcessor.class)
    public TelegramBotUpdatesProcessor telegramBotUpdatesProcessor(UpdateDispatcher updateDispatcher) {
        return new DefaultTelegramBotUpdatesProcessor(updateDispatcher);
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotApiUrlProvider.class)
    public TelegramBotApiUrlProvider telegramBotApiUrlProvider(TelegramBotProperties telegramBotProperties) {
        return new DefaultTelegramBotApiUrlProvider(telegramBotProperties.getToken(),
                telegramBotProperties.getUseTestServer());
    }
}
