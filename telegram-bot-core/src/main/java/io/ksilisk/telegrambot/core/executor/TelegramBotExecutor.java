package io.ksilisk.telegrambot.core.executor;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import io.ksilisk.telegrambot.core.exception.request.TelegramRequestException;

/**
 * Executes Telegram Bot API requests.
 *
 * <p>This is a thin abstraction over the underlying HTTP client / Bot API library.
 * Implementations perform a synchronous call and return the parsed {@link BaseResponse}.</p>
 */
public interface TelegramBotExecutor {
    /**
     * Execute the given request.
     *
     * @param request the request to execute, never {@code null}
     * @param <T> the concrete request type
     * @param <R> the concrete response type
     * @return the successful Telegram response, never {@code null}
     * @throws TelegramRequestException if the request cannot be completed or Telegram returns an error
     */
    <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request) throws TelegramRequestException;
}
