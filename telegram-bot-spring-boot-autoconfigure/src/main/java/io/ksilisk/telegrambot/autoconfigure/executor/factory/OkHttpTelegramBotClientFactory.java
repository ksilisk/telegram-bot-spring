package io.ksilisk.telegrambot.autoconfigure.executor.factory;

import com.pengrad.telegrambot.utility.BotUtils;
import io.ksilisk.telegrambot.autoconfigure.customizer.OkHttpClientCustomizer;
import io.ksilisk.telegrambot.autoconfigure.executor.OkHttpTelegramBotExecutor;
import io.ksilisk.telegrambot.autoconfigure.executor.OkHttpTelegramBotFileClient;
import io.ksilisk.telegrambot.core.executor.TelegramBotClientFactory;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.resolver.TelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.core.file.TelegramBotFileClient;
import io.ksilisk.telegrambot.core.properties.OkHttpClientProperties;
import okhttp3.OkHttpClient;

import java.util.List;

public class OkHttpTelegramBotClientFactory implements TelegramBotClientFactory {
    private final OkHttpClientProperties properties;
    private final List<OkHttpClientCustomizer> customizers;
    private final TelegramBotApiUrlProvider telegramBotApiUrlProvider;

    public OkHttpTelegramBotClientFactory(OkHttpClientProperties properties,
                                          List<OkHttpClientCustomizer> okHttpClientCustomizers,
                                          TelegramBotApiUrlProvider telegramBotApiUrlProvider) {
        this.properties = properties;
        this.customizers = okHttpClientCustomizers;
        this.telegramBotApiUrlProvider = telegramBotApiUrlProvider;
    }

    @Override
    public TelegramBotExecutor createExecutor() {
        return new OkHttpTelegramBotExecutor(createOkHttpClient(), BotUtils.GSON, telegramBotApiUrlProvider);
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(properties.getConnectTimeout())
                .readTimeout(properties.getReadTimeout())
                .writeTimeout(properties.getWriteTimeout())
                .callTimeout(properties.getCallTimeout());
        customizers.forEach(customizer -> customizer.customize(builder));
        return builder.build();

    }

    @Override
    public TelegramBotFileClient createFileClient(TelegramBotExecutor telegramBotExecutor) {
        return new OkHttpTelegramBotFileClient(createOkHttpClient(), telegramBotApiUrlProvider, telegramBotExecutor);
    }
}
