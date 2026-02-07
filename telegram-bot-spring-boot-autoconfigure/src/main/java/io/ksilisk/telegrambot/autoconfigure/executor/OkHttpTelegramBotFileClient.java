package io.ksilisk.telegrambot.autoconfigure.executor;

import io.ksilisk.telegrambot.core.exception.file.TelegramFileDownloadException;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.file.TelegramBotFileClient;
import io.ksilisk.telegrambot.core.file.TelegramBotFiles;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;

public class OkHttpTelegramBotFileClient implements TelegramBotFileClient {

    private final OkHttpClient okHttpClient;
    private final String baseUrl;
    private final TelegramBotExecutor telegramBotExecutor;

    public OkHttpTelegramBotFileClient(OkHttpClient okHttpClient,
                                       String baseUrl,
                                       TelegramBotExecutor telegramBotExecutor) {
        this.okHttpClient = okHttpClient;
        this.baseUrl = baseUrl;
        this.telegramBotExecutor = telegramBotExecutor;
    }

    @Override
    public byte[] downloadByPath(String filePath)  {
        String url = TelegramBotFiles.buildFileUrl(baseUrl, filePath);

        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        Call call = okHttpClient.newCall(request);
        try (Response response = call.execute()) {
            if (!response.isSuccessful()) {
                throw new TelegramFileDownloadException("Failed to download file (" + response.code() + "): " + url);
            }

            ResponseBody body = response.body();
            if (body == null) {
                throw new TelegramFileDownloadException("Empty response body while downloading: " + url);
            }
            return body.bytes();
        } catch (IOException ex) {
            throw new TelegramFileDownloadException("I/O error while downloading file: " + url, ex);
        }
    }

    @Override
    public InputStream openStreamByPath(String filePath) {
        String url = TelegramBotFiles.buildFileUrl(baseUrl, filePath);

        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();

            if (!response.isSuccessful()) {
                try (response) {
                    throw new TelegramFileDownloadException("Failed to download file (" + response.code() + "): " + url);
                }
            }

            ResponseBody body = response.body();
            if (body == null) {
                response.close();
                throw new TelegramFileDownloadException("Empty response body while downloading: " + url);
            }

            return body.byteStream();
        } catch (IOException ex) {
            throw new TelegramFileDownloadException("I/O error while opening file stream: " + url, ex);
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
