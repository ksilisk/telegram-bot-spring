package io.ksilisk.telegrambot.core.registry.rule.message;

import com.pengrad.telegrambot.model.Message;
import io.ksilisk.telegrambot.core.registry.rule.RuleRegistry;
import io.ksilisk.telegrambot.core.rule.MessageRule;

/**
 * Registry of message-based routing rules.
 */
public interface MessageRuleRegistry extends RuleRegistry<MessageRule, Message> {
}
