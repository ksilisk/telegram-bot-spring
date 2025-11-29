package io.ksilisk.telegrambot.core.selector.impl;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.exception.UpdateExceptionHandler;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DefaultUpdateExceptionHandlerSelectorTest {
    @Test
    void shouldReturnSameListInstance() {
        DefaultExceptionHandlerSelector selector = new DefaultExceptionHandlerSelector();

        UpdateExceptionHandler h1 = mock(UpdateExceptionHandler.class);
        UpdateExceptionHandler h2 = mock(UpdateExceptionHandler.class);
        List<UpdateExceptionHandler> handlers = List.of(h1, h2);

        Throwable t = new RuntimeException("test");
        Update update = mock(Update.class);

        List<UpdateExceptionHandler> result = selector.select(handlers, t, update);

        // The selector should return exactly the same list instance
        assertSame(handlers, result, "Selector should return original handlers list unchanged");
    }

    @Test
    void shouldNotModifyListOrFilterAnything() {
        DefaultExceptionHandlerSelector selector = new DefaultExceptionHandlerSelector();

        UpdateExceptionHandler h1 = mock(UpdateExceptionHandler.class);
        UpdateExceptionHandler h2 = mock(UpdateExceptionHandler.class);
        UpdateExceptionHandler h3 = mock(UpdateExceptionHandler.class);

        List<UpdateExceptionHandler> handlers = List.of(h1, h2, h3);

        List<UpdateExceptionHandler> result = selector.select(handlers, new Exception("x"), mock(Update.class));

        assertEquals(3, result.size(), "Selector must not change the number of handlers");
        assertIterableEquals(handlers, result, "Selector must not reorder or alter the list");
    }

    @Test
    void shouldReturnEmptyListAsIs() {
        DefaultExceptionHandlerSelector selector = new DefaultExceptionHandlerSelector();

        List<UpdateExceptionHandler> handlers = List.of();

        List<UpdateExceptionHandler> result = selector.select(handlers, new Exception("x"), mock(Update.class));

        assertTrue(result.isEmpty(), "Selector should return empty list unchanged");
        assertSame(handlers, result, "Empty list should be returned as the same instance");
    }
}
