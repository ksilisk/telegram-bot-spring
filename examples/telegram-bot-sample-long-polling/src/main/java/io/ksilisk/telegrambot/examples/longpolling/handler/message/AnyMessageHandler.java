package io.ksilisk.telegrambot.examples.longpolling.handler.message;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.message.MessageUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import org.springframework.stereotype.Component;

@Component
public class AnyMessageHandler implements MessageUpdateHandler {
    private final TelegramBotExecutor telegramBotExecutor;

    public AnyMessageHandler(TelegramBotExecutor telegramBotExecutor) {
        this.telegramBotExecutor = telegramBotExecutor;
    }

    @Override
    public void handle(Update update) {
        SendMessage sendMessage = new SendMessage(Updates.chatId(update), "Your message is successfully handled!");
        telegramBotExecutor.execute(sendMessage);
    }
}
