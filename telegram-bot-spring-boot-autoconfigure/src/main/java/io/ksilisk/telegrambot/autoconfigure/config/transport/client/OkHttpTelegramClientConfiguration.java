package io.ksilisk.telegrambot.autoconfigure.config.transport.client;

import io.ksilisk.telegrambot.autoconfigure.condition.client.OkHttpSelectedCondition;
import io.ksilisk.telegrambot.autoconfigure.customizer.OkHttpClientCustomizer;
import io.ksilisk.telegrambot.autoconfigure.executor.factory.OkHttpTelegramBotClientFactory;
import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.executor.TelegramBotClientFactory;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.resolver.TelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.core.executor.retry.RetryDelayStrategy;
import io.ksilisk.telegrambot.core.executor.retry.RetryingTelegramBotClientFactory;
import io.ksilisk.telegrambot.core.executor.retry.impl.CompositeRetryRule;
import io.ksilisk.telegrambot.core.file.TelegramBotFileClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@Conditional(OkHttpSelectedCondition.class)
public class OkHttpTelegramClientConfiguration {
    private final TelegramBotApiUrlProvider apiUrlProvider;

    public OkHttpTelegramClientConfiguration(TelegramBotApiUrlProvider apiUrlProvider) {
        this.apiUrlProvider = apiUrlProvider;
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotClientFactory.class)
    public TelegramBotClientFactory telegramBotClientFactory(ObjectProvider<OkHttpClientCustomizer> okHttpClientCustomizers,
                                                             ObjectProvider<CompositeRetryRule> compositeRetryRule,
                                                             ObjectProvider<RetryDelayStrategy> retryDelayStrategy,
                                                             TelegramBotProperties properties) {
        OkHttpTelegramBotClientFactory rawFactory = new OkHttpTelegramBotClientFactory(properties.getClient().getOkhttp(),
                okHttpClientCustomizers.orderedStream().toList(), apiUrlProvider);

        return new RetryingTelegramBotClientFactory(rawFactory,
                properties.getClient().getRetry().getEnabled(),
                compositeRetryRule.getIfAvailable(),
                retryDelayStrategy.getIfAvailable());
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotExecutor.class)
    public TelegramBotExecutor telegramBotExecutor(TelegramBotClientFactory telegramBotClientFactory) {
        return telegramBotClientFactory.createExecutor();
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotFileClient.class)
    public TelegramBotFileClient telegramBotFileClient(TelegramBotClientFactory clientFactory,
                                                       TelegramBotExecutor telegramBotExecutor) {
        return clientFactory.createFileClient(telegramBotExecutor);
    }
}
