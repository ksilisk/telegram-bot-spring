package io.ksilisk.telegrambot.examples.advanced.longpolling.handler.callback;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ClickMeCallbackUpdateHandler implements CallbackUpdateHandler {
    private final TelegramBotExecutor telegramBotExecutor;

    public ClickMeCallbackUpdateHandler(TelegramBotExecutor telegramBotExecutor) {
        this.telegramBotExecutor = telegramBotExecutor;
    }

    @Override
    public void handle(Update update) {
        SendMessage sendMessage = new SendMessage(update.callbackQuery().from().id(),
                "Your callback query was successfully processed");
        telegramBotExecutor.execute(sendMessage);
    }

    @Override
    public Set<String> callbacks() {
        return Set.of("click_me");
    }
}
