package io.ksilisk.telegrambot.core.file;

import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import io.ksilisk.telegrambot.core.exception.file.TelegramFileDownloadException;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class TelegramBotFiles {
    private TelegramBotFiles() {}

    public static String buildFileUrl(String baseUrl, String filePath) {
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("baseUrl must not be null or blank");
        }
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("filePath must not be null or blank");
        }

        String normalizedBase = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";

        String fp = filePath.startsWith("/") ? filePath.substring(1) : filePath;

        int slash = fp.lastIndexOf('/') + 1;
        String path = fp.substring(0, slash);
        String fileName = fp.substring(slash);

        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replace("+", "%20");

        return normalizedBase + path + encodedFileName;
    }

    public static String resolveFilePath(TelegramBotExecutor executor, String fileId) {
        if (executor == null) {
            throw new IllegalArgumentException("executor must not be null");
        }
        if (fileId == null || fileId.isBlank()) {
            throw new IllegalArgumentException("fileId must not be null or blank");
        }

        GetFileResponse response = executor.execute(new GetFile(fileId));

        if (response.file() == null) {
            throw new TelegramFileDownloadException("GetFile returned null file for fileId=" + fileId);
        }

        String filePath = response.file().filePath();
        if (filePath == null || filePath.isBlank()) {
            throw new TelegramFileDownloadException("Telegram did not return file_path for fileId=" + fileId);
        }

        return filePath;
    }
}
