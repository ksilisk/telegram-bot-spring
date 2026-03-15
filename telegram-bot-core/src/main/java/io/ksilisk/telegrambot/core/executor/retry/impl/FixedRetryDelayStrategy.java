package io.ksilisk.telegrambot.core.executor.retry.impl;

import com.pengrad.telegrambot.request.BaseRequest;
import io.ksilisk.telegrambot.core.executor.retry.RetryDelayStrategy;

import java.time.Duration;

/**
 * {@link RetryDelayStrategy} that always returns the same fixed delay
 * between retry attempts.
 */
public class FixedRetryDelayStrategy implements RetryDelayStrategy {
    private final Duration delay;

    public FixedRetryDelayStrategy(Duration delay) {
        this.delay = delay;
    }

    @Override
    public Duration nextDelay(BaseRequest<?, ?> request, Throwable error, int attempt) {
        return delay;
    }
}
