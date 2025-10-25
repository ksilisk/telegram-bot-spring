package io.ksilisk.telegrambot.autoconfigure.config.transport;

import io.ksilisk.telegrambot.autoconfigure.properties.HttpClientProperties;
import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(OkHttpClient.class)
public class HttpClientConfiguration {
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
}
