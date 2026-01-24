package io.ksilisk.telegrambot.examples.webhook.handler.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CallbackCommandHandler implements CommandUpdateHandler {
    private final TelegramBotExecutor telegramBotExecutor;

    public CallbackCommandHandler(TelegramBotExecutor telegramBotExecutor) {
        this.telegramBotExecutor = telegramBotExecutor;
    }


    @Override
    public void handle(Update update) {
        SendMessage sendMessage = new SendMessage(Updates.chatId(update), "Callback button is below");
        InlineKeyboardButton validKeyboardButton = new InlineKeyboardButton();
        validKeyboardButton.callbackData("test");
        validKeyboardButton.setText("Click on me!");

        InlineKeyboardButton invalidKeyboardButton = new InlineKeyboardButton();
        invalidKeyboardButton.setText("Button without a handler. Try it out!");
        invalidKeyboardButton.callbackData("invalid");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.addRow(validKeyboardButton);
        keyboardMarkup.addRow(invalidKeyboardButton);

        telegramBotExecutor.execute(sendMessage.replyMarkup(keyboardMarkup));
    }

    @Override
    public Set<String> commands() {
        return Set.of("/callback");
    }
}
