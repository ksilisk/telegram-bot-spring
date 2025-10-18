package io.ksilisk.telegrambot.core.executor;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;

public interface TelegramBotExecutor {
    <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request);
}
