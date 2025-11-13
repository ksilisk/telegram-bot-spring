package io.ksilisk.telegrambot.autoconfigure.config.transport;

import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.executor.RestClientTelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.resolver.DefaultTelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.core.executor.resolver.TelegramBotApiUrlProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "telegram.bot.client.implementation", havingValue = "SPRING")
@ConditionalOnClass(name = "org.springframework.web.client.RestClient")
public class SpringTelegramClientConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ClientHttpRequestFactory clientHttpRequestFactory(TelegramBotProperties telegramBotProperties) {
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory();
        factory.setReadTimeout(telegramBotProperties.getHttpClient().getReadTimeout());
        return factory;
    }

    @Bean
    @ConditionalOnMissingBean
    public RestClient restClient(ClientHttpRequestFactory factory, TelegramBotApiUrlProvider apiUrlProvider) {
        return RestClient.builder()
                .baseUrl(apiUrlProvider.getApiUrl())
                .requestFactory(factory)
                .build();
    }


    @Bean
    @ConditionalOnMissingBean
    public TelegramBotExecutor telegramBotExecutor(RestClient restClient, TelegramBotApiUrlProvider apiUrlProvider) {
        return new RestClientTelegramBotExecutor(restClient, apiUrlProvider.getApiUrl());
    }


    @Bean
    @ConditionalOnMissingBean(TelegramBotApiUrlProvider.class)
    public TelegramBotApiUrlProvider telegramBotApiUrlProvider(TelegramBotProperties telegramBotProperties) {
        return new DefaultTelegramBotApiUrlProvider(telegramBotProperties.getToken(),
                telegramBotProperties.getUseTestServer());
    }
}

