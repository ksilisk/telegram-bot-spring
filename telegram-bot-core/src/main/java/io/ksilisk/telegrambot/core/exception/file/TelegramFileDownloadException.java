package io.ksilisk.telegrambot.core.exception.file;

public class TelegramFileDownloadException extends RuntimeException {
    public TelegramFileDownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public TelegramFileDownloadException(String message) {
        super(message);
    }
}
