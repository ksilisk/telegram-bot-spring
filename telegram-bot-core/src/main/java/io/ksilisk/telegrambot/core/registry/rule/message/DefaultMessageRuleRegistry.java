package io.ksilisk.telegrambot.core.registry.rule.message;

import com.pengrad.telegrambot.model.Message;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.rule.MessageRule;
import io.ksilisk.telegrambot.core.rule.Rule;

import java.util.*;

/// default implementation of the MessageRuleRegistry
public class DefaultMessageRuleRegistry implements MessageRuleRegistry {
    private final PriorityQueue<MessageRule> messageRules = new PriorityQueue<>(Comparator.comparingInt(Rule::order));

    @Override
    public void register(MessageRule messageRule) {
        messageRules.add(messageRule);

    }

    @Override
    public Optional<UpdateHandler> find(Message message) {
        for (MessageRule messageRule : messageRules) {
            if (messageRule.matcher().match(message)) {
                return Optional.ofNullable(messageRule.updateHandler());
            }
        }
        return Optional.empty();
    }
}
