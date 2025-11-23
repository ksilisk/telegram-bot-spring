package io.ksilisk.telegrambot.autoconfigure.config.transport;

import io.ksilisk.telegrambot.core.delivery.UpdateDelivery;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.ingress.LongPollingUpdateIngress;
import io.ksilisk.telegrambot.core.ingress.impl.DefaultLongPollingUpdateIngress;
import io.ksilisk.telegrambot.core.poller.DefaultUpdatePoller;
import io.ksilisk.telegrambot.core.poller.UpdatePoller;
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

    @Bean(destroyMethod = "") // should be destroyed by LongPollingUpdateIngress
    @ConditionalOnMissingBean(UpdatePoller.class)
    public UpdatePoller updatePoller(OffsetStore offsetStore,
                                     TelegramBotExecutor telegramBotExecutor,
                                     LongPollingProperties longPollingProperties,
                                     UpdateDelivery updateDelivery) {
        return new DefaultUpdatePoller(offsetStore, telegramBotExecutor,
                updateDelivery, longPollingProperties);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean(LongPollingUpdateIngress.class)
    public LongPollingUpdateIngress longPollingUpdateIngress(UpdatePoller updatePoller) {
        return new DefaultLongPollingUpdateIngress(updatePoller);
    }

    @Bean
    @ConditionalOnMissingBean(OffsetStore.class)
    public OffsetStore offsetStore() {
        return new InMemoryOffsetStore();
    }
}
