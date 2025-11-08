package io.ksilisk.telegrambot.examples.webhook.handler.callback;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TestCallbackHandler implements CallbackUpdateHandler {
    private final TelegramBotExecutor telegramBotExecutor;

    public TestCallbackHandler(TelegramBotExecutor telegramBotExecutor) {
        this.telegramBotExecutor = telegramBotExecutor;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.callbackQuery().maybeInaccessibleMessage().chat().id();
        SendMessage sendMessage = new SendMessage(chatId, "Successfully handled callback 'test'");
        telegramBotExecutor.execute(sendMessage);
    }

    @Override
    public Set<String> callbacks() {
        return Set.of("test");
    }
}
