package io.ksilisk.telegrambot.core.router;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.registry.handler.callback.CallbackHandlerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CallbackUpdateRouterTest {
    private CallbackHandlerRegistry callbackHandlerRegistry;
    private CallbackUpdateRouter router;

    private Update update;
    private CallbackQuery callbackQuery;

    @BeforeEach
    void setUp() {
        callbackHandlerRegistry = mock(CallbackHandlerRegistry.class);
        router = new CallbackUpdateRouter(callbackHandlerRegistry);

        update = mock(Update.class);
        callbackQuery = mock(CallbackQuery.class);

        when(update.callbackQuery()).thenReturn(callbackQuery);
    }

    // --------------------------------------------------
    // supports()
    // --------------------------------------------------

    @Test
    void shouldSupportUpdateWhenCallbackQueryIsPresent() {
        when(update.callbackQuery()).thenReturn(callbackQuery);

        assertTrue(router.supports(update), "Router should support updates with callbackQuery");
    }

    @Test
    void shouldNotSupportUpdateWhenCallbackQueryIsNull() {
        when(update.callbackQuery()).thenReturn(null);

        assertFalse(router.supports(update), "Router should not support updates without callbackQuery");
    }

    // --------------------------------------------------
    // route() – normal cases
    // --------------------------------------------------

    @Test
    void shouldRouteToHandlerWhenCallbackHandlerExists() {
        String data = "cb:data";
        UpdateHandler handler = mock(UpdateHandler.class);

        when(callbackQuery.data()).thenReturn(data);
        when(callbackHandlerRegistry.find(data)).thenReturn(Optional.of(handler));

        boolean result = router.route(update);

        assertTrue(result, "Router should return true when a handler is found");
        verify(callbackHandlerRegistry).find(data);
        verify(handler).handle(update);
    }

    @Test
    void shouldReturnFalseWhenNoCallbackHandlerRegistered() {
        String data = "cb:missing";

        when(callbackQuery.data()).thenReturn(data);
        when(callbackHandlerRegistry.find(data)).thenReturn(Optional.empty());

        boolean result = router.route(update);

        assertFalse(result, "Router should return false when no handler is found");
        verify(callbackHandlerRegistry).find(data);
    }

    @Test
    void shouldReturnFalseAndNotInvokeRegistryWhenUpdateIsNotSupported() {
        // Simulate misuse: route(update) is called for an update without callbackQuery
        when(update.callbackQuery()).thenReturn(null);

        boolean result = router.route(update);

        assertFalse(result, "Router should return false when update is not supported");
        // Registry must not be invoked if supports(update) is false
        verifyNoInteractions(callbackHandlerRegistry);
    }
}
