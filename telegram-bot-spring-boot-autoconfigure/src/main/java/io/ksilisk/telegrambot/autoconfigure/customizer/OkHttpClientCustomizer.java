package io.ksilisk.telegrambot.autoconfigure.customizer;

import okhttp3.OkHttpClient;
import org.springframework.core.Ordered;

/**
 * Customizes the {@link OkHttpClient.Builder} used by the Telegram Bot client.
 *
 * <p>Customizers are applied in {@link Ordered} order before the client is built.
 * Define an {@link OkHttpClient} bean to replace the client entirely.
 */
@FunctionalInterface
public interface OkHttpClientCustomizer extends Ordered {

    /**
     * Customize the given {@link OkHttpClient.Builder}.
     *
     * @param builder the builder to customize
     */
    void customize(okhttp3.OkHttpClient.Builder builder);

    @Override
    default int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
