package io.ksilisk.telegrambot.autoconfigure.config.transport.client;

import com.pengrad.telegrambot.utility.BotUtils;
import io.ksilisk.telegrambot.autoconfigure.condition.client.OkHttpSelectedCondition;
import io.ksilisk.telegrambot.autoconfigure.customizer.OkHttpClientCustomizer;
import io.ksilisk.telegrambot.autoconfigure.executor.OkHttpTelegramBotExecutor;
import io.ksilisk.telegrambot.autoconfigure.executor.OkHttpTelegramBotFileClient;
import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.resolver.TelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.core.executor.retry.RetryDelayStrategy;
import io.ksilisk.telegrambot.core.executor.retry.impl.CompositeRetryRule;
import io.ksilisk.telegrambot.core.file.TelegramBotFileClient;
import io.ksilisk.telegrambot.core.properties.OkHttpClientProperties;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import static io.ksilisk.telegrambot.autoconfigure.config.transport.client.TelegramClientRetryConfiguration.buildRetryingTelegramBotExecutor;

@Configuration(proxyBeanMethods = false)
@Conditional(OkHttpSelectedCondition.class)
public class OkHttpTelegramClientConfiguration {
    private final TelegramBotProperties properties;
    private final TelegramBotApiUrlProvider apiUrlProvider;

    public OkHttpTelegramClientConfiguration(TelegramBotProperties properties, TelegramBotApiUrlProvider apiUrlProvider) {
        this.properties = properties;
        this.apiUrlProvider = apiUrlProvider;
    }

    @Bean
    @ConditionalOnMissingBean
    public OkHttpClient okHttpClient(ObjectProvider<OkHttpClientCustomizer> okHttpClientCustomizers) {
        OkHttpClientProperties props = properties.getClient().getOkhttp();

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(props.getConnectTimeout())
                .readTimeout(props.getReadTimeout())
                .writeTimeout(props.getWriteTimeout())
                .callTimeout(props.getCallTimeout());

        okHttpClientCustomizers.orderedStream().forEach(c -> c.customize(builder));
        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotExecutor.class)
    public TelegramBotExecutor telegramBotExecutor(OkHttpClient okHttpClient,
                                                   ObjectProvider<CompositeRetryRule> compositeRetryRule,
                                                   ObjectProvider<RetryDelayStrategy> retryDelayStrategy) {
        TelegramBotExecutor executor =
                new OkHttpTelegramBotExecutor(okHttpClient, BotUtils.GSON, apiUrlProvider.getApiUrl());

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
            OkHttpClient okHttpClient,
            TelegramBotExecutor telegramBotExecutor
    ) {
        return new OkHttpTelegramBotFileClient(
                okHttpClient,
                apiUrlProvider.getFileUrl(),
                telegramBotExecutor
        );
    }
}
