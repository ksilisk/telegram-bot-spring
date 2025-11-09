package io.ksilisk.telegrambot.core.executor;

import com.pengrad.telegrambot.impl.TelegramBotClient;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import io.ksilisk.telegrambot.core.exception.request.RequestFailedException;

public class SimpleTelegramBotExecutor implements TelegramBotExecutor {
    private final TelegramBotClient telegramBot;

    public SimpleTelegramBotExecutor(TelegramBotClient telegramBot) {
        this.telegramBot = telegramBot;
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request) {
        R response = telegramBot.send(request);
        if(response == null) {
            throw new RequestFailedException("Request Failed. No response received");
        } else if (!response.isOk()) {
            String errorMessage = String.format("Request failed. Error code : %d, Reason : %s"
                    ,response.errorCode(), response.description());
            throw new RequestFailedException(errorMessage);
        }
        return response;
    }
}
