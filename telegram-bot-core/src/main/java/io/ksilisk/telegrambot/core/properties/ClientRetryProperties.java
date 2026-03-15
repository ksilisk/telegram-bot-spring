package io.ksilisk.telegrambot.core.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * Configuration properties for Telegram Bot API client retry support.
 */
public class ClientRetryProperties {
    private static final boolean DEFAULT_RETRY_ENABLED = false;
    private static final int DEFAULT_MAX_ATTEMPTS = 3;
    private static final Duration DEFAULT_RETRY_DELAY = Duration.ofSeconds(1);

    /**
     * Enables retry decorator for TelegramBotExecutor.
     */
    private boolean enabled = DEFAULT_RETRY_ENABLED;

    /**
     * Total number of attempts including the first one.
     */
    @Min(1)
    @Max(Integer.MAX_VALUE)
    private int maxAttempts = DEFAULT_MAX_ATTEMPTS;

    /**
     * Explicit allow-list of Bot API method names.
     * Example: getMe, getFile, sendMessage
     */
    private Set<String> retryableMethods = new HashSet<>();

    /**
     * Initial delay before the next retry.
     */
    private Duration delay = DEFAULT_RETRY_DELAY;

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public Set<String> getRetryableMethods() {
        return retryableMethods;
    }

    public void setRetryableMethods(Set<String> retryableMethods) {
        this.retryableMethods = retryableMethods;
    }

    public Duration getDelay() {
        return delay;
    }

    public void setDelay(Duration delay) {
        this.delay = delay;
    }
}
