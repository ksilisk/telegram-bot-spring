package io.ksilisk.telegrambot.core.file;

import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import io.ksilisk.telegrambot.core.exception.file.TelegramFileDownloadException;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TelegramBotFilesTest {
    @Test
    void buildFileUrl_shouldNormalizeBaseUrl_addTrailingSlashIfMissing() {
        String url = TelegramBotFiles.buildFileUrl(
                "https://api.telegram.org/file/botTOKEN",
                "photos/file.jpg"
        );
        assertEquals("https://api.telegram.org/file/botTOKEN/photos/file.jpg", url);
    }

    @Test
    void buildFileUrl_shouldKeepBaseUrlTrailingSlash_ifAlreadyPresent() {
        String url = TelegramBotFiles.buildFileUrl(
                "https://api.telegram.org/file/botTOKEN/",
                "photos/file.jpg"
        );
        assertEquals("https://api.telegram.org/file/botTOKEN/photos/file.jpg", url);
    }

    @Test
    void buildFileUrl_shouldStripLeadingSlashFromFilePath() {
        String url = TelegramBotFiles.buildFileUrl(
                "https://api.telegram.org/file/botTOKEN",
                "/photos/file.jpg"
        );
        assertEquals("https://api.telegram.org/file/botTOKEN/photos/file.jpg", url);
    }

    @Test
    void buildFileUrl_shouldEncodeOnlyFileName_spacesAsPercent20() {
        String url = TelegramBotFiles.buildFileUrl(
                "https://api.telegram.org/file/botTOKEN",
                "photos/my file name.png"
        );
        assertEquals("https://api.telegram.org/file/botTOKEN/photos/my%20file%20name.png", url);
    }

    @Test
    void buildFileUrl_shouldEncodeUnicodeInFileName() {
        String url = TelegramBotFiles.buildFileUrl(
                "https://api.telegram.org/file/botTOKEN",
                "docs/привет мир.txt"
        );
        // URLEncoder encodes UTF-8 bytes; spaces become %20 after replace
        assertTrue(url.startsWith("https://api.telegram.org/file/botTOKEN/docs/"));
        assertTrue(url.contains("%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82%20%D0%BC%D0%B8%D1%80.txt"));
    }

    @Test
    void buildFileUrl_shouldNotEncodePathSeparators() {
        String url = TelegramBotFiles.buildFileUrl(
                "https://api.telegram.org/file/botTOKEN",
                "a/b/c/my file.txt"
        );
        assertEquals("https://api.telegram.org/file/botTOKEN/a/b/c/my%20file.txt", url);
    }

    @Test
    void buildFileUrl_shouldThrowOnBlankBaseUrl() {
        assertThrows(IllegalArgumentException.class, () ->
                TelegramBotFiles.buildFileUrl("  ", "photos/file.jpg"));
    }

    @Test
    void buildFileUrl_shouldThrowOnBlankFilePath() {
        assertThrows(IllegalArgumentException.class, () ->
                TelegramBotFiles.buildFileUrl("https://x", " "));
    }

    @Test
    void resolveFilePath_shouldThrowOnNullExecutor() {
        assertThrows(IllegalArgumentException.class, () ->
                TelegramBotFiles.resolveFilePath(null, "fileId"));
    }

    @Test
    void resolveFilePath_shouldThrowOnBlankFileId() {
        TelegramBotExecutor executor = mock(TelegramBotExecutor.class);
        assertThrows(IllegalArgumentException.class, () ->
                TelegramBotFiles.resolveFilePath(executor, " "));
    }

    @Test
    void resolveFilePath_shouldExecuteGetFileWithGivenId() throws Exception {
        TelegramBotExecutor executor = mock(TelegramBotExecutor.class);
        GetFileResponse response = mock(GetFileResponse.class);
        File file = mock(File.class);

        when(executor.execute(any(GetFile.class))).thenReturn(response);
        when(response.file()).thenReturn(file);
        when(file.filePath()).thenReturn("photos/abc.jpg");

        String path = TelegramBotFiles.resolveFilePath(executor, "ID123");
        assertEquals("photos/abc.jpg", path);

        ArgumentCaptor<GetFile> captor = ArgumentCaptor.forClass(GetFile.class);
        verify(executor).execute(captor.capture());

        assertNotNull(captor.getValue());
    }

    @Test
    void resolveFilePath_shouldThrowWhenResponseFileIsNull() throws Exception {
        TelegramBotExecutor executor = mock(TelegramBotExecutor.class);
        GetFileResponse response = mock(GetFileResponse.class);

        when(executor.execute(any(GetFile.class))).thenReturn(response);
        when(response.file()).thenReturn(null);

        TelegramFileDownloadException ex = assertThrows(TelegramFileDownloadException.class, () ->
                TelegramBotFiles.resolveFilePath(executor, "ID123"));

        assertTrue(ex.getMessage().contains("GetFile returned null file"));
    }

    @Test
    void resolveFilePath_shouldThrowWhenFilePathIsBlank() throws Exception {
        TelegramBotExecutor executor = mock(TelegramBotExecutor.class);
        GetFileResponse response = mock(GetFileResponse.class);
        File file = mock(File.class);

        when(executor.execute(any(GetFile.class))).thenReturn(response);
        when(response.file()).thenReturn(file);
        when(file.filePath()).thenReturn("  ");

        TelegramFileDownloadException ex = assertThrows(TelegramFileDownloadException.class, () ->
                TelegramBotFiles.resolveFilePath(executor, "ID123"));

        assertTrue(ex.getMessage().contains("did not return file_path"));
    }
}
