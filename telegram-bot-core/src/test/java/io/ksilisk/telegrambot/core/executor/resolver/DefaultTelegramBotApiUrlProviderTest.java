package io.ksilisk.telegrambot.core.executor.resolver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultTelegramBotApiUrlProviderTest {
    static String TEST_BOT_TOKEN = "token";

    @Test
    void shouldReturnTestServerApiUrl() {
        TelegramBotApiUrlProvider provider = new DefaultTelegramBotApiUrlProvider(TEST_BOT_TOKEN, true);
        assertEquals("https://api.telegram.org/bot" + TEST_BOT_TOKEN + "/test/", provider.getApiUrl());
    }

    @Test
    void shouldReturnProductionServerApiUrl() {
        TelegramBotApiUrlProvider provider = new DefaultTelegramBotApiUrlProvider(TEST_BOT_TOKEN, false);
        assertEquals("https://api.telegram.org/bot" + TEST_BOT_TOKEN + "/", provider.getApiUrl());
    }
}