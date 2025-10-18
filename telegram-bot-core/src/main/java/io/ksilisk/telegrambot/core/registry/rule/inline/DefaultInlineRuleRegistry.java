package io.ksilisk.telegrambot.core.registry.rule.inline;

import com.pengrad.telegrambot.model.InlineQuery;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.rule.InlineRule;
import io.ksilisk.telegrambot.core.rule.Rule;

import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;

public class DefaultInlineRuleRegistry implements InlineRuleRegistry {
    private final PriorityQueue<InlineRule> inlineRules = new PriorityQueue<>(Comparator.comparingInt(Rule::order));

    @Override
    public void register(InlineRule inlineRule) {
        inlineRules.add(inlineRule);
    }

    @Override
    public Optional<UpdateHandler> find(InlineQuery inlineQuery) {
        for (InlineRule inlineRule : inlineRules) {
            if (inlineRule.matcher().match(inlineQuery)) {
                return Optional.ofNullable(inlineRule.updateHandler());
            }
        }
        return Optional.empty();
    }
}
