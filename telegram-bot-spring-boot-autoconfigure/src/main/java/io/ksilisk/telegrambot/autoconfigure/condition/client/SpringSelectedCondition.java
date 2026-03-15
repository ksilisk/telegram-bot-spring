package io.ksilisk.telegrambot.autoconfigure.condition.client;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Spring Boot condition that activates the Spring {@code RestClient}-based
 * Telegram client configuration.
 *
 * <p>This condition matches when:
 * <ul>
 *   <li>the client implementation is explicitly set to {@code SPRING}; or</li>
 *   <li>the client implementation is {@code AUTO} (or not specified),
 *       OkHttp is not available on the classpath, and Spring
 *       {@code RestClient} is available.</li>
 * </ul>
 *
 * <p>This condition is used as a fallback transport selection for Telegram
 * client auto-configuration.
 */
public final class SpringSelectedCondition extends AnyNestedCondition {

    public SpringSelectedCondition() {
        super(ConfigurationPhase.PARSE_CONFIGURATION);
    }

    @ConditionalOnProperty(name = "telegram.bot.client.implementation", havingValue = "SPRING")
    @ConditionalOnClass(name = "org.springframework.web.client.RestClient")
    static class ExplicitSpring {
    }

    @ConditionalOnProperty(
            name = "telegram.bot.client.implementation",
            havingValue = "AUTO",
            matchIfMissing = true
    )
    @ConditionalOnMissingClass("okhttp3.OkHttpClient")
    @ConditionalOnClass(name = "org.springframework.web.client.RestClient")
    static class AutoSpringFallback {
    }
}
