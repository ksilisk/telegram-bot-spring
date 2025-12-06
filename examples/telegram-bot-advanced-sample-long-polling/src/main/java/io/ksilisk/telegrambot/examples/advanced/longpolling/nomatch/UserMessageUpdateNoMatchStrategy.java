package io.ksilisk.telegrambot.examples.advanced.longpolling.nomatch;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.strategy.UpdateNoMatchStrategy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// important note: @Order annotation will be ignored because here is getOrder method implementation
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class UserMessageUpdateNoMatchStrategy implements UpdateNoMatchStrategy {
    private final TelegramBotExecutor telegramBotExecutor;

    public UserMessageUpdateNoMatchStrategy(TelegramBotExecutor telegramBotExecutor) {
        this.telegramBotExecutor = telegramBotExecutor;
    }

    @Override
    public void handle(Update update) {
        SendMessage sendMessage = new SendMessage(update.message().chat().id(),
                "Not found a handler to process this update");
        telegramBotExecutor.execute(sendMessage);
    }

    @Override
    public boolean terminal() {
        return true;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
