package io.ksilisk.telegrambot.longpolling.configuration;

import io.ksilisk.telegrambot.core.delivery.UpdateDelivery;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.poller.UpdatePoller;
import io.ksilisk.telegrambot.longpolling.executor.ReportingMasterAwareTelegramBotExecutor;
import io.ksilisk.telegrambot.longpolling.failover.MasterManager;
import io.ksilisk.telegrambot.longpolling.ingress.DefaultLongPollingUpdateIngress;
import io.ksilisk.telegrambot.longpolling.ingress.LongPollingUpdateIngress;
import io.ksilisk.telegrambot.longpolling.poller.DefaultUpdatePoller;
import io.ksilisk.telegrambot.longpolling.properties.LongPollingProperties;
import io.ksilisk.telegrambot.longpolling.retry.GetUpdatesExcludedRetryRule;
import io.ksilisk.telegrambot.longpolling.store.InMemoryOffsetStore;
import io.ksilisk.telegrambot.longpolling.store.OffsetStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class LongPollingIngressConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "telegram.bot.long-polling")
    public LongPollingProperties longPollingProperties() {
        return new LongPollingProperties();
    }

    @Bean
    @ConditionalOnMissingBean(UpdatePoller.class)
    public UpdatePoller updatePoller(OffsetStore offsetStore,
                                     TelegramBotExecutor telegramBotExecutor,
                                     LongPollingProperties longPollingProperties,
                                     UpdateDelivery updateDelivery,
                                     MasterManager masterManager) {
        TelegramBotExecutor executor = telegramBotExecutor;

        if (longPollingProperties.getFailover().getEnabled()) {
            executor = new ReportingMasterAwareTelegramBotExecutor(executor, masterManager);
        }

        return new DefaultUpdatePoller(offsetStore, executor,
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

    @Bean
    public GetUpdatesExcludedRetryRule getUpdatesExcludedRetryRule() {
        return new GetUpdatesExcludedRetryRule();
    }
}
