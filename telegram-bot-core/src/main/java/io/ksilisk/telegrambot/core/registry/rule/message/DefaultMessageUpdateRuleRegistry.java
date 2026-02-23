package io.ksilisk.telegrambot.core.registry.rule.message;

import com.pengrad.telegrambot.model.Message;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.order.CoreOrdered;
import io.ksilisk.telegrambot.core.rule.MessageUpdateRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DefaultMessageUpdateRuleRegistry implements MessageUpdateRuleRegistry {
    private static final Logger log = LoggerFactory.getLogger(DefaultMessageUpdateRuleRegistry.class);

    private final List<MessageUpdateRule> messageUpdateRules;

    public DefaultMessageUpdateRuleRegistry(Collection<? extends MessageUpdateRule> rules) {
        List<MessageUpdateRule> sorted = new ArrayList<>(rules);
        sorted.sort(CoreOrdered.COMPARATOR);
        this.messageUpdateRules = List.copyOf(sorted);

        log.debug("{} message rules have been registered", messageUpdateRules.size());
    }

    @Override
    public Optional<UpdateHandler> find(Message message) {
        for (MessageUpdateRule messageUpdateRule : messageUpdateRules) {
            if (messageUpdateRule.matcher().match(message)) {
                return Optional.ofNullable(messageUpdateRule.handler());
            }
        }
        log.debug("Rule for message (id={}) wasn't found", message.messageId());
        return Optional.empty();
    }
}
