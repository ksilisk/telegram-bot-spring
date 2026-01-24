package io.ksilisk.telegrambot.examples.advanced.longpolling.handler.message;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.message.MessageUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import org.springframework.stereotype.Component;

@Component
public class DefaultMessageUpdateHandler implements MessageUpdateHandler {
    private final TelegramBotExecutor telegramBotExecutor;

    public DefaultMessageUpdateHandler(TelegramBotExecutor telegramBotExecutor) {
        this.telegramBotExecutor = telegramBotExecutor;
    }

    @Override
    public void handle(Update update) {
        SendMessage sendMessage = new SendMessage(Updates.chatId(update), "This message isn't supported");
        telegramBotExecutor.execute(sendMessage);
    }
}
