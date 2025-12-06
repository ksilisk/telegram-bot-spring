package io.ksilisk.telegrambot.examples.advanced.longpolling.rule.message;

import com.pengrad.telegrambot.model.Message;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.matcher.Matcher;
import io.ksilisk.telegrambot.core.rule.MessageUpdateRule;
import io.ksilisk.telegrambot.examples.advanced.longpolling.handler.message.TestMessageUpdateHandler;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class TestMessageUpdateRule implements MessageUpdateRule {
    private final TestMessageUpdateHandler updateHandler;

    public TestMessageUpdateRule(TestMessageUpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
    }

    @Override
    public Matcher<Message> matcher() {
        return (m) -> m.text().toLowerCase().contains("test");
    }

    @Override
    public UpdateHandler handler() {
        return updateHandler;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
