package io.ksilisk.telegrambot.core.executor.retry.impl;

import com.pengrad.telegrambot.request.BaseRequest;
import io.ksilisk.telegrambot.core.executor.retry.RetryRule;

/**
 * {@link RetryRule} that rejects retry when the configured maximum number
 * of attempts has been reached.
 */
public class MaxAttemptsRetryRule implements RetryRule {
    private final int maxAttempts;

    public MaxAttemptsRetryRule(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    @Override
    public boolean shouldRetry(BaseRequest<?, ?> request, Throwable error, int attempt) {
        return attempt < maxAttempts;
    }
}
