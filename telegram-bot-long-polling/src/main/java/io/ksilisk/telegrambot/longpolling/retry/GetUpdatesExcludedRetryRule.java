package io.ksilisk.telegrambot.longpolling.retry;

import com.pengrad.telegrambot.request.BaseRequest;
import io.ksilisk.telegrambot.core.executor.retry.RetryRule;

/**
 * {@link RetryRule} that always rejects retry for the {@code getUpdates}
 * Telegram Bot API method.
 *
 * <p>This rule exists to prevent client-level retry from being applied to
 * long-polling requests. Retry for {@code getUpdates} is handled separately
 * by the long-polling transport layer.
 */
public final class GetUpdatesExcludedRetryRule implements RetryRule {
    private static final String GET_UPDATES_METHOD_NAME = "getUpdates";

    @Override
    public boolean shouldRetry(BaseRequest<?, ?> request, Throwable error, int attempt) {
        return !GET_UPDATES_METHOD_NAME.equals(request.getMethod());
    }
}
