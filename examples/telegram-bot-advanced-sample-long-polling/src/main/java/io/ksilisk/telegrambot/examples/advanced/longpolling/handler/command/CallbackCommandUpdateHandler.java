package io.ksilisk.telegrambot.examples.advanced.longpolling.handler.command;

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
public class CallbackCommandUpdateHandler implements CommandUpdateHandler {
    private final TelegramBotExecutor telegramBotExecutor;

    public CallbackCommandUpdateHandler(TelegramBotExecutor telegramBotExecutor) {
        this.telegramBotExecutor = telegramBotExecutor;
    }

    @Override
    public void handle(Update update) {
        SendMessage sendMessage = new SendMessage(Updates.chatId(update), "Callback buttons are below");
        InlineKeyboardButton validKeyboardButton = new InlineKeyboardButton();
        validKeyboardButton.callbackData("click_me");
        validKeyboardButton.setText("Click on me!");

        InlineKeyboardButton invalidKeyboardButton = new InlineKeyboardButton();
        invalidKeyboardButton.setText("Button without a handler. Try it out!");
        invalidKeyboardButton.callbackData("throw_exception");

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
