package io.ksilisk.telegrambot.core.file;

import io.ksilisk.telegrambot.core.exception.file.TelegramFileDownloadException;

import java.io.InputStream;

/**
 * Client for downloading files from Telegram Bot API.
 *
 * <p>{@code *ById(..)} resolves {@code file_id} via {@code getFile} and then downloads the file.
 * {@code *ByPath(..)} downloads directly using an already known {@code file_path}.
 */
public interface TelegramBotFileClient {

    /**
     * Downloads the file content by {@code file_path} into memory.
     *
     * <p>Prefer {@link #openStreamByPath(String)} for large files.
     *
     * @throws TelegramFileDownloadException on download errors
     * @throws IllegalArgumentException if {@code filePath} is null/blank
     */
    byte[] downloadByPath(String filePath);

    /**
     * Opens a download stream by {@code file_path}.
     *
     * <p><b>Caller MUST close</b> the returned stream (try-with-resources), otherwise HTTP resources may leak.
     *
     * @throws TelegramFileDownloadException on download errors
     * @throws IllegalArgumentException if {@code filePath} is null/blank
     */
    InputStream openStreamByPath(String filePath);

    /**
     * Downloads the file content by {@code file_id} into memory.
     *
     * <p>Prefer {@link #openStreamById(String)} for large files.
     *
     * @throws TelegramFileDownloadException on resolve/download errors
     * @throws IllegalArgumentException if {@code fileId} is null/blank
     */
    byte[] downloadById(String fileId);

    /**
     * Opens a download stream by {@code file_id}.
     *
     * <p><b>Caller MUST close</b> the returned stream (try-with-resources), otherwise HTTP resources may leak.
     *
     * @throws TelegramFileDownloadException on resolve/download errors
     * @throws IllegalArgumentException if {@code fileId} is null/blank
     */
    InputStream openStreamById(String fileId);
}
