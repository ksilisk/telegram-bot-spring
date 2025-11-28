package io.ksilisk.telegrambot.core.registry.rule.message;

import com.pengrad.telegrambot.model.Message;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.rule.MessageRule;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultMessageRuleRegistryTest {
    @Test
    void shouldReturnEmptyWhenNoRulesRegistered() {
        Message message = mock(Message.class);
        DefaultMessageRuleRegistry registry = new DefaultMessageRuleRegistry(Collections.emptyList());

        Optional<UpdateHandler> result = registry.find(message);

        assertTrue(result.isEmpty(), "Registry should return empty when no rules are registered");
    }

    @Test
    void shouldReturnHandlerWhenSingleRuleMatches() {
        MessageRule rule = mock(MessageRule.class, RETURNS_DEEP_STUBS);
        Message message = mock(Message.class);
        UpdateHandler handler = mock(UpdateHandler.class);

        // Rule definition
        when(rule.matcher().match(message)).thenReturn(true);
        when(rule.updateHandler()).thenReturn(handler);

        DefaultMessageRuleRegistry registry = new DefaultMessageRuleRegistry(Collections.singletonList(rule));

        Optional<UpdateHandler> result = registry.find(message);

        assertTrue(result.isPresent(), "Matching rule should return a handler");
        assertSame(handler, result.get(), "Returned handler should be the one from the matching rule");
    }

    @Test
    void shouldReturnEmptyWhenNoRulesMatch() {
        MessageRule rule1 = mock(MessageRule.class, RETURNS_DEEP_STUBS);
        MessageRule rule2 = mock(MessageRule.class, RETURNS_DEEP_STUBS);
        Message message = mock(Message.class);

        when(rule1.matcher().match(message)).thenReturn(false);
        when(rule2.matcher().match(message)).thenReturn(false);

        DefaultMessageRuleRegistry registry = new DefaultMessageRuleRegistry(List.of(rule1, rule2));

        Optional<UpdateHandler> result = registry.find(message);

        assertTrue(result.isEmpty(), "If no rule matches, registry should return empty");
    }

    @Test
    void shouldReturnHandlerOfFirstMatchingRule() {
        MessageRule rule1 = mock(MessageRule.class, RETURNS_DEEP_STUBS);
        MessageRule rule2 = mock(MessageRule.class, RETURNS_DEEP_STUBS);
        Message message = mock(Message.class);
        UpdateHandler handler2 = mock(UpdateHandler.class);

        // First rule does not match
        when(rule1.matcher().match(message)).thenReturn(false);

        // Second rule matches
        when(rule2.matcher().match(message)).thenReturn(true);
        when(rule2.updateHandler()).thenReturn(handler2);

        DefaultMessageRuleRegistry registry = new DefaultMessageRuleRegistry(List.of(rule1, rule2));

        Optional<UpdateHandler> result = registry.find(message);

        assertTrue(result.isPresent(), "At least one matching rule should produce a handler");
        assertSame(handler2, result.get(), "Handler from the first matching rule should be returned");
    }

    @Test
    void shouldRespectRuleOrderingWhenMatchingRulesExist() {
        MessageRule lowPriorityRule = mock(MessageRule.class, RETURNS_DEEP_STUBS);
        MessageRule highPriorityRule = mock(MessageRule.class, RETURNS_DEEP_STUBS);
        Message message = mock(Message.class);
        UpdateHandler highPriorityHandler = mock(UpdateHandler.class);

        // Order: lower value means higher priority (based on Comparator.comparingInt(Rule::order))
        when(lowPriorityRule.order()).thenReturn(10);
        when(highPriorityRule.order()).thenReturn(0);

        // Both rules match the message
        when(lowPriorityRule.matcher().match(message)).thenReturn(true);
        when(highPriorityRule.matcher().match(message)).thenReturn(true);

        when(highPriorityRule.updateHandler()).thenReturn(highPriorityHandler);

        DefaultMessageRuleRegistry registry = new DefaultMessageRuleRegistry(List.of(lowPriorityRule, highPriorityRule));


        Optional<UpdateHandler> result = registry.find(message);

        assertTrue(result.isPresent(), "Matching rules should return a handler");
        assertSame(highPriorityHandler, result.get());
    }
}
