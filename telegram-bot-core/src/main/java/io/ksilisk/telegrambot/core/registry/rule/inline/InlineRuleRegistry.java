package io.ksilisk.telegrambot.core.registry.rule.inline;

import com.pengrad.telegrambot.model.InlineQuery;
import io.ksilisk.telegrambot.core.registry.rule.RuleRegistry;
import io.ksilisk.telegrambot.core.rule.InlineRule;

/**
 * Registry of inline query routing rules.
 */
public interface InlineRuleRegistry extends RuleRegistry<InlineRule, InlineQuery> {
}
