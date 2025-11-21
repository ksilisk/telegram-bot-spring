package io.ksilisk.telegrambot.core.handler.exception;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.exception.handler.ExceptionHandlerExecutionException;
import io.ksilisk.telegrambot.core.selector.ExceptionHandlerSelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompositeExceptionHandlerTest {
    private ExceptionHandler handler1;
    private ExceptionHandler handler2;
    private ExceptionHandlerSelector selector;
    private CompositeExceptionHandler compositeHandler;
    private Update update;
    private Throwable throwable;

    @BeforeEach
    void setUp() {
        handler1 = mock(ExceptionHandler.class);
        handler2 = mock(ExceptionHandler.class);
        selector = mock(ExceptionHandlerSelector.class);
        update = mock(Update.class);
        throwable = new RuntimeException("test");

        compositeHandler = new CompositeExceptionHandler(
                List.of(handler1, handler2),
                selector,
                ExceptionHandlerErrorPolicy.LOG
        );
    }

    @Test
    void shouldAlwaysSupportAnyThrowableAndUpdate() {
        boolean result = compositeHandler.supports(throwable, update);
        org.junit.jupiter.api.Assertions.assertTrue(result);
    }

    @Test
    void shouldInvokeOnlyHandlersThatSupportAndRespectTerminalFlag() {
        when(selector.select(List.of(handler1, handler2), throwable, update))
                .thenReturn(List.of(handler1, handler2));

        when(handler1.supports(throwable, update)).thenReturn(true);
        when(handler1.terminal()).thenReturn(true);

        when(handler2.supports(throwable, update)).thenReturn(true);

        compositeHandler.handle(throwable, update);

        verify(selector).select(List.of(handler1, handler2), throwable, update);

        verify(handler1).supports(throwable, update);
        verify(handler1).handle(throwable, update);
        verify(handler1).terminal();

        verifyNoInteractions(handler2);
    }

    @Test
    void shouldSkipHandlerIfItDoesNotSupport() {
        when(selector.select(List.of(handler1, handler2), throwable, update))
                .thenReturn(List.of(handler1, handler2));

        when(handler1.supports(throwable, update)).thenReturn(false);
        when(handler2.supports(throwable, update)).thenReturn(true);
        when(handler2.terminal()).thenReturn(false);

        compositeHandler.handle(throwable, update);

        verify(handler1).supports(throwable, update);
        verify(handler1, never()).handle(any(), any());

        verify(handler2).supports(throwable, update);
        verify(handler2).handle(throwable, update);
        verify(handler2).terminal();
    }

    @Test
    void shouldContinueProcessingHandlersWhenErrorPolicyIsLog() {
        compositeHandler = new CompositeExceptionHandler(
                List.of(handler1, handler2),
                selector,
                ExceptionHandlerErrorPolicy.LOG
        );

        when(selector.select(List.of(handler1, handler2), throwable, update))
                .thenReturn(List.of(handler1, handler2));

        when(handler1.supports(throwable, update)).thenReturn(true);
        when(handler1.terminal()).thenReturn(false);
        doThrow(new RuntimeException("handler failed"))
                .when(handler1).handle(throwable, update);

        when(handler2.supports(throwable, update)).thenReturn(true);
        when(handler2.terminal()).thenReturn(false);

        compositeHandler.handle(throwable, update);

        verify(handler1).supports(throwable, update);
        verify(handler1).handle(throwable, update);

        verify(handler2).supports(throwable, update);
        verify(handler2).handle(throwable, update);
    }

    @Test
    void shouldThrowExceptionHandlerExecutionExceptionWhenErrorPolicyIsThrow() {
        compositeHandler = new CompositeExceptionHandler(
                List.of(handler1, handler2),
                selector,
                ExceptionHandlerErrorPolicy.THROW
        );

        when(selector.select(List.of(handler1, handler2), throwable, update))
                .thenReturn(List.of(handler1, handler2));

        when(handler1.supports(throwable, update)).thenReturn(true);
        when(handler1.terminal()).thenReturn(false);

        doThrow(new RuntimeException("handler failed"))
                .when(handler1).handle(throwable, update);

        assertThrows(ExceptionHandlerExecutionException.class,
                () -> compositeHandler.handle(throwable, update));

        verifyNoInteractions(handler2);
    }
}
