package io.ksilisk.telegrambot.examples.webhook.rule;

import com.pengrad.telegrambot.model.Message;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.matcher.Matcher;
import io.ksilisk.telegrambot.core.rule.MessageRule;
import io.ksilisk.telegrambot.examples.webhook.handler.message.AnyMessageHandler;
import org.springframework.stereotype.Component;

@Component
public class AnyMessageRule implements MessageRule {
    private final AnyMessageHandler anyMessageHandler;

    public AnyMessageRule(AnyMessageHandler anyMessageHandler) {
        this.anyMessageHandler = anyMessageHandler;
    }

    @Override
    public Matcher<Message> matcher() {
        return update -> true; // handle any message
    }

    @Override
    public UpdateHandler updateHandler() {
        return anyMessageHandler;
    }
}
