package io.ksilisk.telegrambot.core.registry.rule.inline;

import com.pengrad.telegrambot.model.InlineQuery;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.order.CoreOrdered;
import io.ksilisk.telegrambot.core.rule.InlineUpdateRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DefaultInlineUpdateRuleRegistry implements InlineUpdateRuleRegistry {
    private static final Logger log = LoggerFactory.getLogger(DefaultInlineUpdateRuleRegistry.class);

    private final List<InlineUpdateRule> inlineUpdateRules;

    public DefaultInlineUpdateRuleRegistry(Collection<? extends InlineUpdateRule> inlineRules) {
        List<InlineUpdateRule> sorted = new ArrayList<>(inlineRules);
        sorted.sort(CoreOrdered.COMPARATOR);
        this.inlineUpdateRules = List.copyOf(sorted);

        log.debug("{} inline rules have been registered", inlineUpdateRules.size());
    }

    @Override
    public Optional<UpdateHandler> find(InlineQuery inlineQuery) {
        for (InlineUpdateRule inlineUpdateRule : inlineUpdateRules) {
            if (inlineUpdateRule.matcher().match(inlineQuery)) {
                return Optional.ofNullable(inlineUpdateRule.handler());
            }
        }
        log.debug("Rule for inline query (id={}) wasn't found", inlineQuery.id());
        return Optional.empty();
    }
}
