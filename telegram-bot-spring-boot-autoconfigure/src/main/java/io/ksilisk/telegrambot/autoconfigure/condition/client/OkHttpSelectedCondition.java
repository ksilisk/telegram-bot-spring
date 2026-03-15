package io.ksilisk.telegrambot.autoconfigure.condition.client;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Spring Boot condition that activates the OkHttp-based Telegram client
 * configuration.
 *
 * <p>This condition matches when:
 * <ul>
 *   <li>the client implementation is explicitly set to {@code OKHTTP}; or</li>
 *   <li>the client implementation is {@code AUTO} (or not specified)
 *       and OkHttp is available on the classpath.</li>
 * </ul>
 *
 * <p>This condition is used to select the OkHttp transport branch for
 * Telegram client auto-configuration.
 */
public final class OkHttpSelectedCondition extends AnyNestedCondition {

    public OkHttpSelectedCondition() {
        super(ConfigurationPhase.PARSE_CONFIGURATION);
    }

    @ConditionalOnProperty(name = "telegram.bot.client.implementation", havingValue = "OKHTTP")
    @ConditionalOnClass(name = "okhttp3.OkHttpClient")
    static class ExplicitOkHttp {
    }

    @ConditionalOnProperty(
            name = "telegram.bot.client.implementation",
            havingValue = "AUTO",
            matchIfMissing = true
    )
    @ConditionalOnClass(name = "okhttp3.OkHttpClient")
    static class AutoOkHttp {
    }
}
