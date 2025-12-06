package io.ksilisk.telegrambot.examples.advanced.longpolling.handler.exception;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.exception.UpdateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class UserMessageUpdateExceptionHandler implements UpdateExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(UserMessageUpdateExceptionHandler.class);

    private final TelegramBotExecutor telegramBotExecutor;

    public UserMessageUpdateExceptionHandler(TelegramBotExecutor telegramBotExecutor) {
        this.telegramBotExecutor = telegramBotExecutor;
    }


    @Override
    public boolean supports(Throwable t, Update update) {
        return update.message() != null;
    }

    @Override
    public void handle(Throwable t, Update update) {
        log.info("Sending fallback message to user");
        SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Internal bot exception.");
        telegramBotExecutor.execute(sendMessage);
    }

    @Override
    public boolean terminal() {
        return true;
    }
}
