package io.ksilisk.telegrambot.core.executor.retry.impl;

import com.pengrad.telegrambot.request.BaseRequest;
import io.ksilisk.telegrambot.core.executor.retry.RetryRule;

import java.util.Set;

/**
 * {@link RetryRule} that allows retry only for Telegram Bot API methods
 * explicitly configured as retryable.
 *
 * <p>This rule acts as a method allow-list. If the request method is not
 * included in the configured retryable methods set, retry is rejected.
 */
public class RetryableMethodsRetryRule implements RetryRule {
    private final Set<String> retryableMethods;

    public RetryableMethodsRetryRule(Set<String> retryableMethods) {
        this.retryableMethods = retryableMethods;
    }

    @Override
    public boolean shouldRetry(BaseRequest<?, ?> request, Throwable error, int attempt) {
        String method = request.getMethod();

        if (method == null || method.isBlank()) {
            return false;
        }

        return retryableMethods.contains(method);
    }
}
