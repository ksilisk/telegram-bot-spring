package io.ksilisk.telegrambot.autoconfigure.executor.factory;

import com.pengrad.telegrambot.utility.BotUtils;
import io.ksilisk.telegrambot.autoconfigure.executor.RestClientTelegramBotExecutor;
import io.ksilisk.telegrambot.autoconfigure.executor.RestClientTelegramBotFileClient;
import io.ksilisk.telegrambot.core.executor.TelegramBotClientFactory;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.resolver.TelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.core.file.TelegramBotFileClient;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestClient;

public class RestClientTelegramBotClientFactory implements TelegramBotClientFactory {
    private final RestClient.Builder restClientBuilder;
    private final TelegramBotApiUrlProvider telegramBotApiUrlProvider;

    public RestClientTelegramBotClientFactory(RestClient.Builder restClientBuilder,
                                              TelegramBotApiUrlProvider telegramBotApiUrlProvider) {
        this.restClientBuilder = restClientBuilder;
        this.telegramBotApiUrlProvider = telegramBotApiUrlProvider;
    }

    @Override
    public TelegramBotExecutor createExecutor() {
        return new RestClientTelegramBotExecutor(createRestClient(), telegramBotApiUrlProvider);

    }

    @Override
    public TelegramBotFileClient createFileClient(TelegramBotExecutor telegramBotExecutor) {
        return new RestClientTelegramBotFileClient(createRestClient(), telegramBotApiUrlProvider, telegramBotExecutor);
    }

    private RestClient createRestClient() {
        return restClientBuilder
                .clone()
                .messageConverters(converters -> {
                    converters.clear();
                    converters.add(new GsonHttpMessageConverter(BotUtils.GSON));
                })
                .build();
    }
}
