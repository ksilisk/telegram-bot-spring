package io.ksilisk.telegrambot.autoconfigure.config.transport;

import com.pengrad.telegrambot.impl.TelegramBotClient;
import com.pengrad.telegrambot.utility.BotUtils;
import io.ksilisk.telegrambot.autoconfigure.properties.HttpClientProperties;
import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.executor.SimpleTelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.resolver.DefaultTelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.core.executor.resolver.TelegramBotApiUrlProvider;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class TelegramClientConfiguration {
    @Bean
    @ConditionalOnMissingBean(OkHttpClient.class)
    public OkHttpClient okHttpClient(TelegramBotProperties telegramBotProperties) {
        HttpClientProperties properties = telegramBotProperties.getHttpClient();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(properties.getConnectTimeout());
        builder.readTimeout(properties.getReadTimeout());
        builder.writeTimeout(properties.getWriteTimeout());
        builder.callTimeout(properties.getCallTimeout());

        return builder.build();
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
    @ConditionalOnMissingBean(TelegramBotApiUrlProvider.class)
    public TelegramBotApiUrlProvider telegramBotApiUrlProvider(TelegramBotProperties telegramBotProperties) {
        return new DefaultTelegramBotApiUrlProvider(telegramBotProperties.getToken(),
                telegramBotProperties.getUseTestServer());
    }
}
