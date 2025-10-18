package io.ksilisk.telegrambot.core.router;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.registry.rule.inline.InlineRuleRegistry;

import java.util.Optional;

public class InlineUpdateRouter implements UpdateRouter {
    private final InlineRuleRegistry inlineRuleRegistry;

    public InlineUpdateRouter(InlineRuleRegistry inlineRuleRegistry) {
        this.inlineRuleRegistry = inlineRuleRegistry;
    }

    @Override
    public boolean supports(Update update) {
        return update.inlineQuery() != null;
    }

    @Override
    public boolean route(Update update) {
        Optional<UpdateHandler> updateHandler = inlineRuleRegistry.find(update.inlineQuery());
        if (updateHandler.isEmpty()) {
            return false;
        }
        updateHandler.get().handle(update);
        return true;
    }
}
