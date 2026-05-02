package io.ksilisk.telegrambot.autoconfigure.executor;

import io.ksilisk.telegrambot.core.exception.file.TelegramFileDownloadException;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.resolver.TelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.core.file.TelegramBotFileClient;
import io.ksilisk.telegrambot.core.file.TelegramBotFiles;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;

public class RestClientTelegramBotFileClient implements TelegramBotFileClient {
    private final RestClient restClient;
    private final TelegramBotApiUrlProvider urlProvider;
    private final TelegramBotExecutor telegramBotExecutor;

    public RestClientTelegramBotFileClient(RestClient restClient,
                                           TelegramBotApiUrlProvider apiUrlProvider,
                                           TelegramBotExecutor telegramBotExecutor) {
        this.restClient = restClient;
        this.urlProvider = apiUrlProvider;
        this.telegramBotExecutor = telegramBotExecutor;
    }

    @Override
    public byte[] downloadByPath(String filePath) {
        String url = TelegramBotFiles.buildFileUrl(urlProvider.getFileUrl(), filePath);

        try {
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(byte[].class);
        } catch (Exception e) {
            throw new TelegramFileDownloadException("Failed to download file: " + url, e);
        }
    }

    @Override
    public InputStream openStreamByPath(String filePath) {
        String url = TelegramBotFiles.buildFileUrl(urlProvider.getFileUrl(), filePath);

        try {
            return restClient.method(HttpMethod.GET)
                    .uri(url)
                    .exchange((request, response) -> {
                        int status = response.getStatusCode().value();
                        if (status < 200 || status >= 300) {
                            throw new IOException("Failed to download file (" + status + "): " + url);
                        }

                        return response.getBody();
                    });
        } catch (Exception e) {
            throw new TelegramFileDownloadException("Failed to open file stream: " + url, e);
        }
    }

    @Override
    public byte[] downloadById(String fileId) {
        return downloadByPath(TelegramBotFiles.resolveFilePath(telegramBotExecutor, fileId));
    }

    @Override
    public InputStream openStreamById(String fileId) {
        return openStreamByPath(TelegramBotFiles.resolveFilePath(telegramBotExecutor, fileId));
    }
}
