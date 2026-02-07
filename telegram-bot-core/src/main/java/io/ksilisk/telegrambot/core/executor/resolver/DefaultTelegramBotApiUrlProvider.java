package io.ksilisk.telegrambot.core.executor.resolver;

public class DefaultTelegramBotApiUrlProvider implements TelegramBotApiUrlProvider {
    private static final String API_URL = "https://api.telegram.org/bot";
    private static final String FILE_URL = "https://api.telegram.org/file/bot";

    private final String botToken;
    private final boolean testServer;

    public DefaultTelegramBotApiUrlProvider(String botToken, boolean testServer) {
        this.botToken = botToken;
        this.testServer = testServer;
    }

    @Override
    public String getApiUrl() {
        return API_URL + botToken + (testServer ? "/test/" : "/");
    }

    @Override
    public String getFileUrl() {
        return FILE_URL + botToken +  (testServer ? "/test/" : "/");
    }
}
