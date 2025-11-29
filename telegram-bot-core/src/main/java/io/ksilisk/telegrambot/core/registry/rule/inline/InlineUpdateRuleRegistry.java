package io.ksilisk.telegrambot.core.registry.rule.inline;

import com.pengrad.telegrambot.model.InlineQuery;
import io.ksilisk.telegrambot.core.registry.rule.UpdateRuleRegistry;
import io.ksilisk.telegrambot.core.rule.InlineUpdateRule;

/**
 * Registry of inline query routing rules.
 */
public interface InlineUpdateRuleRegistry extends UpdateRuleRegistry<InlineUpdateRule, InlineQuery> {
}
