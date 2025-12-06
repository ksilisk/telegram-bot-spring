package io.ksilisk.telegrambot.examples.advanced.longpolling.rule.message;

import com.pengrad.telegrambot.model.Message;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.matcher.Matcher;
import io.ksilisk.telegrambot.core.rule.MessageUpdateRule;
import io.ksilisk.telegrambot.examples.advanced.longpolling.handler.message.DefaultMessageUpdateHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class DefaultMessageUpdateRule implements MessageUpdateRule {
    private final DefaultMessageUpdateHandler updateHandler;

    public DefaultMessageUpdateRule(DefaultMessageUpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
    }

    @Override
    public Matcher<Message> matcher() {
        return (m) -> true;
    }

    @Override
    public UpdateHandler handler() {
        return updateHandler;
    }
}
