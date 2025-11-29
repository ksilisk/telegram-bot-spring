package io.ksilisk.telegrambot.core.registry.rule.message;

import com.pengrad.telegrambot.model.Message;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.rule.MessageUpdateRule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DefaultMessageUpdateRuleRegistry implements MessageUpdateRuleRegistry {
    private static final Comparator<MessageUpdateRule> RULE_COMPARATOR =
            Comparator.comparingInt(MessageUpdateRule::order);

    private final List<MessageUpdateRule> messageUpdateRules;

    public DefaultMessageUpdateRuleRegistry(Collection<? extends MessageUpdateRule> rules) {
        List<MessageUpdateRule> sorted = new ArrayList<>(rules);
        sorted.sort(RULE_COMPARATOR);
        this.messageUpdateRules = List.copyOf(sorted);
    }

    @Override
    public Optional<UpdateHandler> find(Message message) {
        for (MessageUpdateRule messageUpdateRule : messageUpdateRules) {
            if (messageUpdateRule.matcher().match(message)) {
                return Optional.ofNullable(messageUpdateRule.handler());
            }
        }
        return Optional.empty();
    }
}
