package io.ksilisk.telegrambot.autoconfigure.config.transport;

import com.pengrad.telegrambot.impl.TelegramBotClient;
import com.pengrad.telegrambot.utility.BotUtils;
import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.delivery.UpdateDelivery;
import io.ksilisk.telegrambot.core.dispatcher.UpdateDispatcher;
import io.ksilisk.telegrambot.core.executor.SimpleTelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.resolver.DefaultTelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.core.executor.resolver.TelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.core.poller.DefaultUpdatePoller;
import io.ksilisk.telegrambot.core.poller.UpdatePoller;
import io.ksilisk.telegrambot.core.processor.DefaultTelegramBotUpdatesProcessor;
import io.ksilisk.telegrambot.core.processor.TelegramBotUpdatesProcessor;
import io.ksilisk.telegrambot.core.store.InMemoryOffsetStore;
import io.ksilisk.telegrambot.core.store.OffsetStore;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "LONG_POLLING", matchIfMissing = true)
public class LongPollingConfiguration {
    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean(UpdatePoller.class)
    public UpdatePoller updatePoller(OffsetStore offsetStore,
                                     TelegramBotExecutor telegramBotExecutor,
                                     TelegramBotProperties telegramBotProperties,
                                     UpdateDelivery updateDelivery) {
        return new DefaultUpdatePoller(offsetStore, telegramBotExecutor,
                updateDelivery, telegramBotProperties.getLongPolling());
    }

    @Bean
    @ConditionalOnMissingBean(OffsetStore.class)
    public OffsetStore offsetStore() {
        return new InMemoryOffsetStore();
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
