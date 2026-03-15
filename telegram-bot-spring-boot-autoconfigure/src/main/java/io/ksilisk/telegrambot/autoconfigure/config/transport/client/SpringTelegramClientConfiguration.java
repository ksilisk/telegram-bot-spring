package io.ksilisk.telegrambot.autoconfigure.config.transport.client;

import com.pengrad.telegrambot.utility.BotUtils;
import io.ksilisk.telegrambot.autoconfigure.condition.client.SpringSelectedCondition;
import io.ksilisk.telegrambot.autoconfigure.executor.RestClientTelegramBotExecutor;
import io.ksilisk.telegrambot.autoconfigure.executor.RestClientTelegramBotFileClient;
import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.resolver.TelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.core.executor.retry.RetryDelayStrategy;
import io.ksilisk.telegrambot.core.executor.retry.impl.CompositeRetryRule;
import io.ksilisk.telegrambot.core.file.TelegramBotFileClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestClient;

import static io.ksilisk.telegrambot.autoconfigure.config.transport.client.TelegramClientRetryConfiguration.buildRetryingTelegramBotExecutor;

@Configuration(proxyBeanMethods = false)
@Conditional(SpringSelectedCondition.class)
public class SpringTelegramClientConfiguration {

    private final TelegramBotProperties properties;
    private final TelegramBotApiUrlProvider apiUrlProvider;

    public SpringTelegramClientConfiguration(
            TelegramBotProperties properties,
            TelegramBotApiUrlProvider apiUrlProvider
    ) {
        this.properties = properties;
        this.apiUrlProvider = apiUrlProvider;
    }

    @Bean
    @ConditionalOnMissingBean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .baseUrl(apiUrlProvider.getApiUrl())
                .messageConverters(converters -> {
                    converters.clear();
                    converters.add(new GsonHttpMessageConverter(BotUtils.GSON));
                })
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotExecutor.class)
    public TelegramBotExecutor telegramBotExecutor(RestClient restClient,
                                                   ObjectProvider<CompositeRetryRule> compositeRetryRule,
                                                   ObjectProvider<RetryDelayStrategy> retryDelayStrategy) {
        TelegramBotExecutor executor =
                new RestClientTelegramBotExecutor(restClient, apiUrlProvider.getApiUrl());

        if (properties.getClient().getRetry().getEnabled()) {
            return buildRetryingTelegramBotExecutor(executor,
                    compositeRetryRule.getIfAvailable(),
                    retryDelayStrategy.getIfAvailable());
        }

        return executor;
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotFileClient.class)
    public TelegramBotFileClient telegramBotFileClient(
            RestClient restClient,
            TelegramBotExecutor telegramBotExecutor
    ) {
        return new RestClientTelegramBotFileClient(
                restClient,
                apiUrlProvider.getFileUrl(),
                telegramBotExecutor
        );
    }
}
