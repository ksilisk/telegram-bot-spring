package io.ksilisk.telegrambot.examples.webhook.rule;

import com.pengrad.telegrambot.model.InlineQuery;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.matcher.Matcher;
import io.ksilisk.telegrambot.core.rule.InlineUpdateRule;
import io.ksilisk.telegrambot.examples.webhook.handler.inline.AnyInlineHandler;
import org.springframework.stereotype.Component;

@Component
public class AnyInlineUpdateRule implements InlineUpdateRule {
    private final AnyInlineHandler anyInlineHandler;

    public AnyInlineUpdateRule(AnyInlineHandler anyInlineHandler) {
        this.anyInlineHandler = anyInlineHandler;
    }

    @Override
    public Matcher<InlineQuery> matcher() {
        return query -> true;
    }

    @Override
    public UpdateHandler handler() {
        return anyInlineHandler;
    }
}
