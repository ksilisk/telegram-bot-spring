package io.ksilisk.telegrambot.autoconfigure.config.transport.client;

import io.ksilisk.telegrambot.autoconfigure.condition.client.SpringSelectedCondition;
import io.ksilisk.telegrambot.autoconfigure.executor.factory.RestClientTelegramBotClientFactory;
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
import org.springframework.web.client.RestClient;

@Configuration(proxyBeanMethods = false)
@Conditional(SpringSelectedCondition.class)
public class SpringTelegramClientConfiguration {

    private final TelegramBotApiUrlProvider apiUrlProvider;

    public SpringTelegramClientConfiguration(TelegramBotApiUrlProvider apiUrlProvider) {
        this.apiUrlProvider = apiUrlProvider;
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotClientFactory.class)
    public TelegramBotClientFactory telegramBotClientFactory(RestClient.Builder restClientBuilder,
                                                             ObjectProvider<CompositeRetryRule> compositeRetryRule,
                                                             ObjectProvider<RetryDelayStrategy> retryDelayStrategy,
                                                             TelegramBotProperties properties) {
        TelegramBotClientFactory rawFactory = new RestClientTelegramBotClientFactory(restClientBuilder, apiUrlProvider);

        return new RetryingTelegramBotClientFactory(rawFactory,
                properties.getClient().getRetry().getEnabled(),
                compositeRetryRule.getIfAvailable(),
                retryDelayStrategy.getIfAvailable());
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotExecutor.class)
    public TelegramBotExecutor telegramBotExecutor(TelegramBotClientFactory clientFactory) {
        return clientFactory.createExecutor();
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotFileClient.class)
    public TelegramBotFileClient telegramBotFileClient(TelegramBotClientFactory clientFactory,
                                                       TelegramBotExecutor telegramBotExecutor) {
        return clientFactory.createFileClient(telegramBotExecutor);
    }
}
