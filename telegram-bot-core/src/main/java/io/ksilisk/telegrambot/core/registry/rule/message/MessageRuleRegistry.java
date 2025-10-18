package io.ksilisk.telegrambot.core.registry.rule.message;

import com.pengrad.telegrambot.model.Message;
import io.ksilisk.telegrambot.core.registry.rule.RuleRegistry;
import io.ksilisk.telegrambot.core.rule.MessageRule;

/// a registry that contains rules to match a plain text message with a handler
public interface MessageRuleRegistry extends RuleRegistry<MessageRule, Message> {
}
