package io.ksilisk.telegrambot.autoconfigure.config.transport;

import io.ksilisk.telegrambot.core.delivery.UpdateDelivery;
import io.ksilisk.telegrambot.core.dispatcher.UpdateDispatcher;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.poller.DefaultUpdatePoller;
import io.ksilisk.telegrambot.core.poller.UpdatePoller;
import io.ksilisk.telegrambot.core.processor.DefaultTelegramBotUpdatesProcessor;
import io.ksilisk.telegrambot.core.processor.TelegramBotUpdatesProcessor;
import io.ksilisk.telegrambot.core.properties.LongPollingProperties;
import io.ksilisk.telegrambot.core.store.InMemoryOffsetStore;
import io.ksilisk.telegrambot.core.store.OffsetStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "LONG_POLLING", matchIfMissing = true)
public class LongPollingConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "telegram.bot.long-polling")
    public LongPollingProperties longPollingProperties() {
        return new LongPollingProperties();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean(UpdatePoller.class)
    public UpdatePoller updatePoller(OffsetStore offsetStore,
                                     TelegramBotExecutor telegramBotExecutor,
                                     LongPollingProperties longPollingProperties,
                                     UpdateDelivery updateDelivery) {
        return new DefaultUpdatePoller(offsetStore, telegramBotExecutor,
                updateDelivery, longPollingProperties);
    }

    @Bean
    @ConditionalOnMissingBean(OffsetStore.class)
    public OffsetStore offsetStore() {
        return new InMemoryOffsetStore();
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotUpdatesProcessor.class)
    public TelegramBotUpdatesProcessor telegramBotUpdatesProcessor(UpdateDispatcher updateDispatcher) {
        return new DefaultTelegramBotUpdatesProcessor(updateDispatcher);
    }
}
