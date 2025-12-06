package io.ksilisk.telegrambot.examples.advanced.longpolling.handler.message;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.message.MessageUpdateHandler;
import org.springframework.stereotype.Component;

@Component
public class TestMessageUpdateHandler implements MessageUpdateHandler {
    private final TelegramBotExecutor telegramBotExecutor;

    public TestMessageUpdateHandler(TelegramBotExecutor telegramBotExecutor) {
        this.telegramBotExecutor = telegramBotExecutor;
    }

    @Override
    public void handle(Update update) {
        SendMessage sendMessage = new SendMessage(update.message().chat().id(),
                "Your 'test' message was successfully handled");
        telegramBotExecutor.execute(sendMessage);
    }
}
