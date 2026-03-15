package io.ksilisk.telegrambot.core.executor.retry;

import com.pengrad.telegrambot.request.BaseRequest;

import java.time.Duration;

/**
 * Strategy interface that calculates the delay before the next retry attempt.
 *
 * <p>Implementations may use a fixed delay, exponential backoff,
 * jitter, or any custom retry timing policy.
 */
public interface RetryDelayStrategy {
    /**
     * Returns the delay to wait before the next retry attempt.
     *
     * @param request the original Telegram Bot API request
     * @param error the failure raised during request execution
     * @param attempt the current attempt number, starting from {@code 1}
     * @return the delay before the next retry attempt
     */
    Duration nextDelay(BaseRequest<?, ?> request, Throwable error, int attempt);
}
