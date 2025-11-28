package io.ksilisk.telegrambot.core.registry.rule.inline;

import com.pengrad.telegrambot.model.InlineQuery;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.rule.InlineRule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DefaultInlineRuleRegistry implements InlineRuleRegistry {
    private static final Comparator<InlineRule> RULE_COMPARATOR =
            Comparator.comparingInt(InlineRule::order);

    private final List<InlineRule> inlineRules;

    public DefaultInlineRuleRegistry(Collection<? extends InlineRule> inlineRules) {
        List<InlineRule> sorted = new ArrayList<>(inlineRules);
        sorted.sort(RULE_COMPARATOR);
        this.inlineRules = List.copyOf(sorted);
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
