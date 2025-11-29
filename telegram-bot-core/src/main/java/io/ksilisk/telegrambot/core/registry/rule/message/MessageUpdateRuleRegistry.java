package io.ksilisk.telegrambot.core.registry.rule.message;

import com.pengrad.telegrambot.model.Message;
import io.ksilisk.telegrambot.core.registry.rule.UpdateRuleRegistry;
import io.ksilisk.telegrambot.core.rule.MessageUpdateRule;

/**
 * Registry of message-based routing rules.
 */
public interface MessageUpdateRuleRegistry extends UpdateRuleRegistry<MessageUpdateRule, Message> {
}
