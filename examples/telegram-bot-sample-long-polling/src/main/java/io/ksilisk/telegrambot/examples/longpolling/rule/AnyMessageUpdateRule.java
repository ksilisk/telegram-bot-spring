package io.ksilisk.telegrambot.examples.longpolling.rule;

import com.pengrad.telegrambot.model.Message;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.matcher.Matcher;
import io.ksilisk.telegrambot.core.rule.MessageUpdateRule;
import io.ksilisk.telegrambot.examples.longpolling.handler.message.AnyMessageHandler;
import org.springframework.stereotype.Component;

@Component
public class AnyMessageUpdateRule implements MessageUpdateRule {
    private final AnyMessageHandler anyMessageHandler;

    public AnyMessageUpdateRule(AnyMessageHandler anyMessageHandler) {
        this.anyMessageHandler = anyMessageHandler;
    }

    @Override
    public Matcher<Message> matcher() {
        return update -> true; // handle any message
    }

    @Override
    public UpdateHandler handler() {
        return anyMessageHandler;
    }
}
