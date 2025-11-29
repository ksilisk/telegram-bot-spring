package io.ksilisk.telegrambot.core.router;

import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.registry.rule.inline.InlineUpdateRuleRegistry;
import io.ksilisk.telegrambot.core.router.impl.DefaultInlineUpdateRouter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultInlineUpdateRouterTest {
    private InlineUpdateRuleRegistry inlineRuleRegistry;
    private DefaultInlineUpdateRouter router;
    private Update update;
    private InlineQuery inlineQuery;

    @BeforeEach
    void setUp() {
        inlineRuleRegistry = mock(InlineUpdateRuleRegistry.class);
        router = new DefaultInlineUpdateRouter(inlineRuleRegistry);

        update = mock(Update.class);
        inlineQuery = mock(InlineQuery.class);

        when(update.inlineQuery()).thenReturn(inlineQuery);
    }

    // --------------------------------------------------
    // supports()
    // --------------------------------------------------

    @Test
    void shouldSupportUpdateWhenInlineQueryIsPresent() {
        when(update.inlineQuery()).thenReturn(inlineQuery);

        assertTrue(router.supports(update), "Router should support updates with inline query");
    }

    @Test
    void shouldNotSupportUpdateWhenInlineQueryIsNull() {
        when(update.inlineQuery()).thenReturn(null);

        assertFalse(router.supports(update), "Router should not support updates without inline query");
    }

    // --------------------------------------------------
    // route()
    // --------------------------------------------------

    @Test
    void shouldRouteToHandlerWhenInlineRuleMatches() {
        UpdateHandler handler = mock(UpdateHandler.class);

        when(inlineRuleRegistry.find(inlineQuery)).thenReturn(Optional.of(handler));

        boolean result = router.route(update);

        assertTrue(result, "Router should return true when a handler is found");
        verify(inlineRuleRegistry).find(inlineQuery);
        verify(handler).handle(update);
    }

    @Test
    void shouldReturnFalseWhenNoInlineRuleMatches() {
        when(inlineRuleRegistry.find(inlineQuery)).thenReturn(Optional.empty());

        boolean result = router.route(update);

        assertFalse(result, "Router should return false when no handler is found");
        verify(inlineRuleRegistry).find(inlineQuery);
    }

    @Test
    void shouldReturnFalseWhenInlineQueryIsNullEvenIfRouteIsCalledDirectly() {
        // This simulates misuse of the router API:
        // route(update) is called without checking supports(update) first.
        when(update.inlineQuery()).thenReturn(null);
        when(inlineRuleRegistry.find(null)).thenReturn(Optional.empty());

        boolean result = router.route(update);

        assertFalse(result, "Router should return false when inlineQuery is null");
    }
}
