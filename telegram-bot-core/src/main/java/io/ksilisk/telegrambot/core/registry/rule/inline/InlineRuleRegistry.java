package io.ksilisk.telegrambot.core.registry.rule.inline;

import com.pengrad.telegrambot.model.InlineQuery;
import io.ksilisk.telegrambot.core.registry.rule.RuleRegistry;
import io.ksilisk.telegrambot.core.rule.InlineRule;

/// a registry that contains rules to match an inline query update with a handler
public interface InlineRuleRegistry extends RuleRegistry<InlineRule, InlineQuery> {
}
