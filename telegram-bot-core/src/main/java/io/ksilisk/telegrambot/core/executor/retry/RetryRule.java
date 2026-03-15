package io.ksilisk.telegrambot.core.executor.retry;

import com.pengrad.telegrambot.request.BaseRequest;
import io.ksilisk.telegrambot.core.order.CoreOrdered;

/**
 * Strategy interface that decides whether a failed Telegram Bot API request
 * should be retried.
 *
 * <p>Implementations typically evaluate the request type, thrown error,
 * current attempt number, or any combination of these factors.
 *
 * <p>Rules are usually combined through a composite implementation.
 * If a rule returns {@code false}, retry is rejected.
 *
 * <p>This contract also extends {@link CoreOrdered}, so multiple rules
 * can be evaluated in a deterministic order.
 */
public interface RetryRule extends CoreOrdered {
    /**
     * Determines whether the given failed request should be retried.
     *
     * @param request the original Telegram Bot API request
     * @param error the failure raised during request execution
     * @param attempt the current attempt number, starting from {@code 1}
     * @return {@code true} if retry is allowed, otherwise {@code false}
     */
    boolean shouldRetry(BaseRequest<?, ?> request, Throwable error, int attempt);
}
