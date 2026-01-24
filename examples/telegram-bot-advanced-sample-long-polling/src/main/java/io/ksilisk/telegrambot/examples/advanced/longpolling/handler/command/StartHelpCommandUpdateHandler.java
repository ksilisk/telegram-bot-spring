package io.ksilisk.telegrambot.examples.advanced.longpolling.handler.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class StartHelpCommandUpdateHandler implements CommandUpdateHandler {
    private final TelegramBotExecutor telegramBotExecutor;

    public StartHelpCommandUpdateHandler(TelegramBotExecutor telegramBotExecutor) {
        this.telegramBotExecutor = telegramBotExecutor;
    }

    @Override
    public void handle(Update update) {
        SendMessage sendMessage = new SendMessage(Updates.chatId(update), """
                Hello! I'm a simple bot. \
                Try these commands:
                /throw - to throw and error in handler
                /help - to the this message again
                /callback - to get message with callback buttons
                /some - to get fallback message by NoMatchStrategy
                Also, try to send a message that contains 'test'""");
        telegramBotExecutor.execute(sendMessage);
    }

    @Override
    public Set<String> commands() {
        return Set.of("/start", "/help");
    }
}
