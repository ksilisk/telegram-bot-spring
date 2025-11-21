package io.ksilisk.telegrambot.core.executor.resolver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultTelegramBotApiUrlProviderTest {
    @Test
    void shouldReturnProductionUrlWhenTestServerIsFalse() {
        String token = "123:ABC";
        DefaultTelegramBotApiUrlProvider provider =
                new DefaultTelegramBotApiUrlProvider(token, false);

        String url = provider.getApiUrl();

        assertEquals("https://api.telegram.org/bot123:ABC/", url);
    }

    @Test
    void shouldReturnTestServerUrlWhenTestServerIsTrue() {
        String token = "987:XYZ";
        DefaultTelegramBotApiUrlProvider provider =
                new DefaultTelegramBotApiUrlProvider(token, true);

        String url = provider.getApiUrl();

        assertEquals("https://api.telegram.org/bot987:XYZ/test/", url);
    }

    @Test
    void shouldWorkWithEmptyToken() {
        DefaultTelegramBotApiUrlProvider provider =
                new DefaultTelegramBotApiUrlProvider("", false);

        String url = provider.getApiUrl();

        assertEquals("https://api.telegram.org/bot/", url);
    }

    @Test
    void shouldWorkWithSpecialCharactersInToken() {
        String token = "*weird-token*/";
        DefaultTelegramBotApiUrlProvider provider =
                new DefaultTelegramBotApiUrlProvider(token, false);

        String url = provider.getApiUrl();

        assertEquals("https://api.telegram.org/bot*weird-token*//", url);
    }
}
