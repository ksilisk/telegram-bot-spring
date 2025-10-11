package io.ksilisk.telegrambot.core.api;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;

public class BotGateway {
    private final TelegramBot telegramBot;

    public BotGateway(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request) {
        return telegramBot.execute(request);
    }
}
