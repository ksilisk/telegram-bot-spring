package io.ksilisk.telegrambot.core.executor.retry;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import io.ksilisk.telegrambot.core.exception.request.TelegramRequestException;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.retry.impl.CompositeRetryRule;

import java.time.Duration;

/**
 * {@link TelegramBotExecutor} decorator that retries failed Telegram Bot API
 * requests according to the configured {@link RetryRule} and
 * {@link RetryDelayStrategy}.
 *
 * <p>This executor delegates request execution to another
 * {@link TelegramBotExecutor}. When execution fails, it evaluates the retry
 * rule and, if retry is allowed, waits for the configured delay before
 * performing the next attempt.
 *
 * <p>This decorator does not define retry policy by itself. Retry decisions
 * are fully delegated to the provided {@link RetryRule} implementation.
 */
public class RetryingTelegramBotExecutor implements TelegramBotExecutor {
    private final TelegramBotExecutor delegate;
    private final CompositeRetryRule retryRule;
    private final RetryDelayStrategy retryDelayStrategy;

    public RetryingTelegramBotExecutor(TelegramBotExecutor delegate,
                                       CompositeRetryRule retryRule,
                                       RetryDelayStrategy retryDelayStrategy) {
        this.delegate = delegate;
        this.retryRule = retryRule;
        this.retryDelayStrategy = retryDelayStrategy;
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request)
            throws TelegramRequestException {
        int attempt = 1;

        while (true) {
            try {
                return delegate.execute(request);
            } catch (Exception ex) {
                if (!retryRule.shouldRetry(request, ex, attempt)) {
                    throw ex;
                }

                Duration delay = retryDelayStrategy.nextDelay(request, ex, attempt);
                sleep(delay);
                attempt++;
            }
        }
    }

    private void sleep(Duration delay) {
        try {
            Thread.sleep(delay.toMillis());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Retry sleep was interrupted", ex);
        }
    }
}
