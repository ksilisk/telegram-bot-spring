package io.ksilisk.telegrambot.core.registry.rule.inline;

import com.pengrad.telegrambot.model.InlineQuery;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.rule.InlineUpdateRule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DefaultInlineUpdateRuleRegistry implements InlineUpdateRuleRegistry {
    private static final Comparator<InlineUpdateRule> RULE_COMPARATOR =
            Comparator.comparingInt(InlineUpdateRule::order);

    private final List<InlineUpdateRule> inlineUpdateRules;

    public DefaultInlineUpdateRuleRegistry(Collection<? extends InlineUpdateRule> inlineRules) {
        List<InlineUpdateRule> sorted = new ArrayList<>(inlineRules);
        sorted.sort(RULE_COMPARATOR);
        this.inlineUpdateRules = List.copyOf(sorted);
    }

    @Override
    public Optional<UpdateHandler> find(InlineQuery inlineQuery) {
        for (InlineUpdateRule inlineUpdateRule : inlineUpdateRules) {
            if (inlineUpdateRule.matcher().match(inlineQuery)) {
                return Optional.ofNullable(inlineUpdateRule.handler());
            }
        }
        return Optional.empty();
    }
}
