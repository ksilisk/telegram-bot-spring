package io.ksilisk.telegrambot.examples.webhook.handler.command;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ExceptionCommandHandler implements CommandUpdateHandler {
    @Override
    public void handle(Update update) {
        throw new IllegalStateException("Some unexpected exception");
    }

    @Override
    public Set<String> commands() {
        return Set.of("/throw", "/exception");
    }
}
