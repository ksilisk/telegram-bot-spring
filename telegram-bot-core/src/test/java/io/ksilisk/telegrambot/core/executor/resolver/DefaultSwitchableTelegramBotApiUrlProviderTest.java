package io.ksilisk.telegrambot.core.executor.resolver;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultSwitchableTelegramBotApiUrlProviderTest {
    @Test
    void shouldUseDefaultEndpointWhenEndpointsAreNull() {
        DefaultSwitchableTelegramBotApiUrlProvider provider =
                new DefaultSwitchableTelegramBotApiUrlProvider("token", false, null);
        assertThat(provider.getApiUrl())
                .isEqualTo("https://api.telegram.org/bottoken/");
        assertThat(provider.getFileUrl())
                .isEqualTo("https://api.telegram.org/file/bottoken/");
    }

    @Test
    void shouldUseDefaultEndpointWhenEndpointsAreEmpty() {
        DefaultSwitchableTelegramBotApiUrlProvider provider =
                new DefaultSwitchableTelegramBotApiUrlProvider("token", false, List.of());
        assertThat(provider.getApiUrl())
                .isEqualTo("https://api.telegram.org/bottoken/");
        assertThat(provider.getFileUrl())
                .isEqualTo("https://api.telegram.org/file/bottoken/");
    }

    @Test
    void shouldBuildApiUrlFromConfiguredEndpoint() {
        DefaultSwitchableTelegramBotApiUrlProvider provider =
                new DefaultSwitchableTelegramBotApiUrlProvider(
                        "token",
                        false,
                        List.of("https://tg-api-1.internal")
                );
        assertThat(provider.getApiUrl())
                .isEqualTo("https://tg-api-1.internal/bottoken/");
    }

    @Test
    void shouldBuildFileUrlFromConfiguredEndpoint() {
        DefaultSwitchableTelegramBotApiUrlProvider provider =
                new DefaultSwitchableTelegramBotApiUrlProvider(
                        "token",
                        false,
                        List.of("https://tg-api-1.internal")
                );
        assertThat(provider.getFileUrl())
                .isEqualTo("https://tg-api-1.internal/file/bottoken/");
    }

    @Test
    void shouldAppendTestSuffixWhenTestServerIsEnabled() {
        DefaultSwitchableTelegramBotApiUrlProvider provider =
                new DefaultSwitchableTelegramBotApiUrlProvider(
                        "token",
                        true,
                        List.of("https://tg-api-1.internal")
                );
        assertThat(provider.getApiUrl())
                .isEqualTo("https://tg-api-1.internal/bottoken/test/");
        assertThat(provider.getFileUrl())
                .isEqualTo("https://tg-api-1.internal/file/bottoken/test/");
    }

    @Test
    void shouldNormalizeTrailingSlashes() {
        DefaultSwitchableTelegramBotApiUrlProvider provider =
                new DefaultSwitchableTelegramBotApiUrlProvider(
                        "token",
                        false,
                        List.of("https://tg-api-1.internal///")
                );
        assertThat(provider.getApiUrl())
                .isEqualTo("https://tg-api-1.internal/bottoken/");
        assertThat(provider.getFileUrl())
                .isEqualTo("https://tg-api-1.internal/file/bottoken/");
    }

    @Test
    void shouldSwitchToNextEndpointSequentially() {
        DefaultSwitchableTelegramBotApiUrlProvider provider =
                new DefaultSwitchableTelegramBotApiUrlProvider(
                        "token",
                        false,
                        List.of(
                                "https://tg-api-1.internal",
                                "https://tg-api-2.internal",
                                "https://tg-api-3.internal"
                        )
                );
        assertThat(provider.getApiUrl())
                .isEqualTo("https://tg-api-1.internal/bottoken/");
        provider.switchToNext();
        assertThat(provider.getApiUrl())
                .isEqualTo("https://tg-api-2.internal/bottoken/");
        provider.switchToNext();
        assertThat(provider.getApiUrl())
                .isEqualTo("https://tg-api-3.internal/bottoken/");
    }

    @Test
    void shouldSwitchCyclically() {
        DefaultSwitchableTelegramBotApiUrlProvider provider =
                new DefaultSwitchableTelegramBotApiUrlProvider(
                        "token",
                        false,
                        List.of(
                                "https://tg-api-1.internal",
                                "https://tg-api-2.internal"
                        )
                );
        assertThat(provider.getApiUrl())
                .isEqualTo("https://tg-api-1.internal/bottoken/");
        provider.switchToNext();
        assertThat(provider.getApiUrl())
                .isEqualTo("https://tg-api-2.internal/bottoken/");
        provider.switchToNext();
        assertThat(provider.getApiUrl())
                .isEqualTo("https://tg-api-1.internal/bottoken/");
    }

    @Test
    void shouldNotSwitchWhenOnlyOneEndpointConfigured() {
        DefaultSwitchableTelegramBotApiUrlProvider provider =
                new DefaultSwitchableTelegramBotApiUrlProvider(
                        "token",
                        false,
                        List.of("https://tg-api-1.internal")
                );
        provider.switchToNext();
        assertThat(provider.getApiUrl())
                .isEqualTo("https://tg-api-1.internal/bottoken/");
    }

    @Test
    void shouldDeduplicateNormalizedEndpoints() {
        DefaultSwitchableTelegramBotApiUrlProvider provider =
                new DefaultSwitchableTelegramBotApiUrlProvider(
                        "token",
                        false,
                        List.of(
                                "https://tg-api-1.internal",
                                "https://tg-api-1.internal/",
                                "https://tg-api-2.internal"
                        )
                );
        assertThat(provider.getApiUrl())
                .isEqualTo("https://tg-api-1.internal/bottoken/");
        provider.switchToNext();
        assertThat(provider.getApiUrl())
                .isEqualTo("https://tg-api-2.internal/bottoken/");
        provider.switchToNext();
        assertThat(provider.getApiUrl())
                .isEqualTo("https://tg-api-1.internal/bottoken/");
    }
}
