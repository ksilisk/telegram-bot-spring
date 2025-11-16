package io.ksilisk.telegrambot.autoconfigure.config.transport;

import com.pengrad.telegrambot.utility.BotUtils;
import io.ksilisk.telegrambot.autoconfigure.executor.RestClientTelegramBotExecutor;
import io.ksilisk.telegrambot.autoconfigure.properties.HttpClientProperties;
import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.autoconfigure.executor.OkHttpTelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.resolver.DefaultTelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.core.executor.resolver.TelegramBotApiUrlProvider;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestClient;

@Configuration(proxyBeanMethods = false)
public class TelegramClientConfiguration {

    @Bean
    @ConditionalOnMissingBean(TelegramBotApiUrlProvider.class)
    public TelegramBotApiUrlProvider telegramBotApiUrlProvider(TelegramBotProperties telegramBotProperties) {
        return new DefaultTelegramBotApiUrlProvider(telegramBotProperties.getToken(),
                telegramBotProperties.getUseTestServer());
    }

    @Configuration
    @ConditionalOnProperty(name = "telegram.bot.client.implementation", havingValue = "AUTO", matchIfMissing =  true)
    public static class AutoTelegramClientConfiguration {
        @Configuration
        @ConditionalOnClass(name = "okhttp3.OkHttpClient")
        @ConditionalOnMissingBean(TelegramBotExecutor.class)
        @Order(1)
        public static class PreferOkHttp {
            @Bean
            @ConditionalOnMissingBean
            public OkHttpClient okHttpClient(TelegramBotProperties properties) {
                HttpClientProperties props = properties.getClient().getHttpClient();
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
        @ConditionalOnMissingClass("okhttp3.OkHttpClient")
        @ConditionalOnClass(name = "org.springframework.web.client.RestClient")
        @ConditionalOnMissingBean(TelegramBotExecutor.class)
        @Order(2)
        public static class FallbackToSpring {

            @Bean
            @ConditionalOnMissingBean
            public RestClient restClient(RestClient.Builder builder,TelegramBotApiUrlProvider apiUrlProvider) {
                return builder
                        .baseUrl(apiUrlProvider.getApiUrl())
                        .messageConverters(converters -> {
                            converters.clear();
                            converters.add(new GsonHttpMessageConverter(BotUtils.GSON));
                        })
                        .build();
            }

            @Bean
            @ConditionalOnMissingBean
            public TelegramBotExecutor telegramBotExecutor(RestClient restClient, TelegramBotApiUrlProvider apiUrlProvider) {
                return new RestClientTelegramBotExecutor(restClient, apiUrlProvider.getApiUrl());
            }
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "telegram.bot.client.implementation", havingValue = "SPRING")
    @ConditionalOnClass(name = "org.springframework.web.client.RestClient")
    public static class SpringTelegramClientConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public RestClient restClient(RestClient.Builder builder,TelegramBotApiUrlProvider apiUrlProvider) {
            return builder
                    .baseUrl(apiUrlProvider.getApiUrl())
                    .messageConverters(converters -> {
                        converters.clear();
                        converters.add(new GsonHttpMessageConverter(BotUtils.GSON));
                    })
                    .build();
        }

        @Bean
        @ConditionalOnMissingBean
        public TelegramBotExecutor telegramBotExecutor(RestClient restClient, TelegramBotApiUrlProvider apiUrlProvider) {
            return new RestClientTelegramBotExecutor(restClient, apiUrlProvider.getApiUrl());
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "telegram.bot.client.implementation", havingValue = "OKHTTP")
    @ConditionalOnClass(name = "okhttp3.OkHttpClient")
    public static class OkHttpTelegramClientConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public OkHttpClient okHttpClient(TelegramBotProperties properties) {
            HttpClientProperties props = properties.getClient().getHttpClient();
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
}
