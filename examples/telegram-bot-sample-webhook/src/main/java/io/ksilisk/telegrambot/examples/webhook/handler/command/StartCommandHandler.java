package io.ksilisk.telegrambot.examples.webhook.handler.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class StartCommandHandler implements CommandUpdateHandler {
    private final TelegramBotExecutor telegramBotExecutor;

    public StartCommandHandler(TelegramBotExecutor telegramBotExecutor) {
        this.telegramBotExecutor = telegramBotExecutor;
    }

    @Override
    public void handle(Update update) {
        SendMessage sendMessage = new SendMessage(update.message().from().id(), "Simple Hello!");
        telegramBotExecutor.execute(sendMessage);
    }

    @Override
    public Set<String> commands() {
        return Set.of("/start", "/help");
    }
}
