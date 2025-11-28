package io.ksilisk.telegrambot.core.registry.rule.message;

import com.pengrad.telegrambot.model.Message;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.rule.MessageRule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DefaultMessageRuleRegistry implements MessageRuleRegistry {
    private static final Comparator<MessageRule> RULE_COMPARATOR =
            Comparator.comparingInt(MessageRule::order);

    private final List<MessageRule> messageRules;

    public DefaultMessageRuleRegistry(Collection<? extends MessageRule> rules) {
        List<MessageRule> sorted = new ArrayList<>(rules);
        sorted.sort(RULE_COMPARATOR);
        this.messageRules = List.copyOf(sorted);
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
