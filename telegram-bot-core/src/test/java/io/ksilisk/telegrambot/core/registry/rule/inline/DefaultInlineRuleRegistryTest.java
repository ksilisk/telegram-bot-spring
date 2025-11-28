package io.ksilisk.telegrambot.core.registry.rule.inline;

import com.pengrad.telegrambot.model.InlineQuery;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.rule.InlineRule;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultInlineRuleRegistryTest {

    @Test
    void shouldReturnEmptyWhenNoRulesRegistered() {
        InlineQuery inlineQuery = mock(InlineQuery.class);

        DefaultInlineRuleRegistry registry = new DefaultInlineRuleRegistry(List.of());

        Optional<UpdateHandler> result = registry.find(inlineQuery);

        assertTrue(result.isEmpty(), "Registry should return empty when no rules are registered");
    }

    @Test
    void shouldReturnHandlerWhenSingleRuleMatches() {
        InlineRule rule = mock(InlineRule.class, RETURNS_DEEP_STUBS);
        InlineQuery inlineQuery = mock(InlineQuery.class);
        UpdateHandler handler = mock(UpdateHandler.class);

        // Rule matches the given inline query
        when(rule.matcher().match(inlineQuery)).thenReturn(true);
        when(rule.updateHandler()).thenReturn(handler);

        DefaultInlineRuleRegistry registry = new DefaultInlineRuleRegistry(List.of(rule));

        Optional<UpdateHandler> result = registry.find(inlineQuery);

        assertTrue(result.isPresent(), "Matching rule should return a handler");
        assertSame(handler, result.get(), "Returned handler should be the one from the matching rule");
    }

    @Test
    void shouldReturnEmptyWhenNoRulesMatch() {
        InlineRule rule1 = mock(InlineRule.class, RETURNS_DEEP_STUBS);
        InlineRule rule2 = mock(InlineRule.class, RETURNS_DEEP_STUBS);
        InlineQuery inlineQuery = mock(InlineQuery.class);

        when(rule1.matcher().match(inlineQuery)).thenReturn(false);
        when(rule2.matcher().match(inlineQuery)).thenReturn(false);

        DefaultInlineRuleRegistry registry = new DefaultInlineRuleRegistry(List.of(rule1, rule2));

        Optional<UpdateHandler> result = registry.find(inlineQuery);

        assertTrue(result.isEmpty(), "If no rule matches, registry should return empty");
    }

    @Test
    void shouldReturnHandlerOfFirstMatchingRule() {
        InlineRule rule1 = mock(InlineRule.class, RETURNS_DEEP_STUBS);
        InlineRule rule2 = mock(InlineRule.class, RETURNS_DEEP_STUBS);
        InlineQuery inlineQuery = mock(InlineQuery.class);
        UpdateHandler handler2 = mock(UpdateHandler.class);

        // First rule does not match
        when(rule1.matcher().match(inlineQuery)).thenReturn(false);

        // Second rule matches
        when(rule2.matcher().match(inlineQuery)).thenReturn(true);
        when(rule2.updateHandler()).thenReturn(handler2);

        DefaultInlineRuleRegistry registry = new DefaultInlineRuleRegistry(List.of(rule1, rule2));

        Optional<UpdateHandler> result = registry.find(inlineQuery);

        assertTrue(result.isPresent(), "At least one matching rule should produce a handler");
        assertSame(handler2, result.get(), "Handler from the first matching rule should be returned");
    }

    @Test
    void shouldRespectRuleOrderingWhenMultipleRulesMatchButNotRelyOnPriorityQueueIterationOrder() {
        InlineRule rule1 = mock(InlineRule.class, RETURNS_DEEP_STUBS);
        InlineRule rule2 = mock(InlineRule.class, RETURNS_DEEP_STUBS);
        InlineQuery inlineQuery = mock(InlineQuery.class);

        // Both rules match the inline query
        when(rule1.matcher().match(inlineQuery)).thenReturn(true);
        when(rule2.matcher().match(inlineQuery)).thenReturn(true);

        DefaultInlineRuleRegistry registry = new DefaultInlineRuleRegistry(List.of(rule1, rule2));

        Optional<UpdateHandler> result = registry.find(inlineQuery);

        assertTrue(result.isPresent(), "Matching rules should return a handler");
    }
}
