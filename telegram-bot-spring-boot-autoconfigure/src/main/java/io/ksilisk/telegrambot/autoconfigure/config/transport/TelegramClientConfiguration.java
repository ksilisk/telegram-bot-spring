package io.ksilisk.telegrambot.autoconfigure.config.transport;

import com.pengrad.telegrambot.utility.BotUtils;
import io.ksilisk.telegrambot.autoconfigure.properties.HttpClientProperties;
import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.executor.OkHttpTelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.resolver.DefaultTelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.core.executor.resolver.TelegramBotApiUrlProvider;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "telegram.bot.client.implementation", havingValue = "AUTO", matchIfMissing = true)
public class TelegramClientConfiguration {

    @Configuration
    @ConditionalOnClass(name = "okhttp3.OkHttpClient")
    @ConditionalOnMissingBean(TelegramBotExecutor.class)
    @Order(1)
    public static class OkHttpAutoConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public OkHttpClient okHttpClient(TelegramBotProperties properties) {
            HttpClientProperties props = properties.getHttpClient();
            return new OkHttpClient.Builder()
                    .connectTimeout(props.getConnectTimeout())
                    .readTimeout(props.getReadTimeout())
                    .writeTimeout(props.getWriteTimeout())
                    .callTimeout(props.getCallTimeout())
                    .build();
        }

        @Bean
        @ConditionalOnMissingBean
        public TelegramBotExecutor telegramBotExecutor(OkHttpClient okHttpClient, TelegramBotApiUrlProvider apiUrlProvider) {
            return new OkHttpTelegramBotExecutor(okHttpClient, BotUtils.GSON, apiUrlProvider.getApiUrl());
        }
    }

    @Configuration
    @ConditionalOnClass(name = "org.springframework.web.client.RestClient")
    @ConditionalOnMissingBean(TelegramBotExecutor.class)
    @Order(2)
    public static class SpringAutoConfig {
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
    }



    @Bean
    @ConditionalOnMissingBean(TelegramBotApiUrlProvider.class)
    public TelegramBotApiUrlProvider telegramBotApiUrlProvider(TelegramBotProperties telegramBotProperties) {
        return new DefaultTelegramBotApiUrlProvider(telegramBotProperties.getToken(),
                telegramBotProperties.getUseTestServer());
    }
}
