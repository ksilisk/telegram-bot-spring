package io.ksilisk.telegrambot.core.executor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;

public class SimpleTelegramBotExecutor implements TelegramBotExecutor {
    private final TelegramBot telegramBot;

    public SimpleTelegramBotExecutor(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request) {
        return telegramBot.execute(request);
    }
}
