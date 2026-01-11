package io.ksilisk.telegrambot.kafka.handler;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.exception.UpdateExceptionHandler;
import org.springframework.core.Ordered;

public class KafkaUpdateExceptionHandler implements UpdateExceptionHandler {

    @Override
    public boolean supports(Throwable t, Update update) {
        return true;
    }

    @Override
    public void handle(Throwable t, Update update) {

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
