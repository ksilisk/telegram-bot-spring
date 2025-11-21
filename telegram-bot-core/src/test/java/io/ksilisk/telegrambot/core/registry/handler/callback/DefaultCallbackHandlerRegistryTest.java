package io.ksilisk.telegrambot.core.registry.handler.callback;

import io.ksilisk.telegrambot.core.exception.registry.CallbackHandlerAlreadyExists;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultCallbackHandlerRegistryTest {
    private DefaultCallbackHandlerRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new DefaultCallbackHandlerRegistry();
    }

    @Test
    void shouldRegisterHandlerForSingleCallback() {
        CallbackUpdateHandler handler = mock(CallbackUpdateHandler.class);

        // Handler declares a single callback key
        when(handler.callbacks()).thenReturn(Set.of("cb:one"));

        registry.register(handler);

        Optional<UpdateHandler> result = registry.find("cb:one");

        assertTrue(result.isPresent(), "Handler should be found for registered callback");
        assertSame(handler, result.get(), "Returned handler should be the same instance as registered");
    }

    @Test
    void shouldRegisterHandlerForMultipleCallbacks() {
        CallbackUpdateHandler handler = mock(CallbackUpdateHandler.class);

        // Handler is responsible for multiple callback_data values
        when(handler.callbacks()).thenReturn(Set.of("cb:one", "cb:two", "cb:three"));

        registry.register(handler);

        assertTrue(registry.find("cb:one").isPresent());
        assertTrue(registry.find("cb:two").isPresent());
        assertTrue(registry.find("cb:three").isPresent());

        assertSame(handler, registry.find("cb:one").get());
        assertSame(handler, registry.find("cb:two").get());
        assertSame(handler, registry.find("cb:three").get());
    }

    @Test
    void shouldReturnEmptyWhenCallbackNotRegistered() {
        Optional<UpdateHandler> result = registry.find("unknown");

        assertTrue(result.isEmpty(), "Registry should return empty optional for unknown callback");
    }

    @Test
    void shouldThrowWhenRegisteringDuplicateCallback() {
        CallbackUpdateHandler firstHandler = mock(CallbackUpdateHandler.class);
        CallbackUpdateHandler secondHandler = mock(CallbackUpdateHandler.class);

        when(firstHandler.callbacks()).thenReturn(Set.of("cb:dup"));
        when(secondHandler.callbacks()).thenReturn(Set.of("cb:dup"));

        registry.register(firstHandler);

        // Second registration with the same callback key should fail
        CallbackHandlerAlreadyExists ex = assertThrows(
                CallbackHandlerAlreadyExists.class,
                () -> registry.register(secondHandler),
                "Expected duplicate callback registration to throw CallbackHandlerAlreadyExists"
        );

        assertTrue(
                ex.getMessage().contains("cb:dup"),
                "Exception message should mention the conflicting callback_data"
        );
    }

    @Test
    void shouldAllowDifferentHandlersForDifferentCallbacks() {
        CallbackUpdateHandler firstHandler = mock(CallbackUpdateHandler.class);
        CallbackUpdateHandler secondHandler = mock(CallbackUpdateHandler.class);

        when(firstHandler.callbacks()).thenReturn(Set.of("cb:first"));
        when(secondHandler.callbacks()).thenReturn(Set.of("cb:second"));

        registry.register(firstHandler);
        registry.register(secondHandler);

        assertSame(firstHandler, registry.find("cb:first").orElseThrow());
        assertSame(secondHandler, registry.find("cb:second").orElseThrow());
    }
}
