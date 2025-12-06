package io.ksilisk.telegrambot.examples.advanced.longpolling.handler.command;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ThrowCommandUpdateHandler implements CommandUpdateHandler {
    @Override
    public void handle(Update update) {
        throw new IllegalStateException("Some exception");
    }

    @Override
    public Set<String> commands() {
        return Set.of("/throw");
    }
}
