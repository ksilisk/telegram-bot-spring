package io.ksilisk.telegrambot.core.interceptor;

import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompositeUpdateInterceptorTest {
    private UpdateInterceptor interceptor1;
    private UpdateInterceptor interceptor2;
    private UpdateInterceptor interceptor3;
    private CompositeUpdateInterceptor composite;
    private Update update;

    @BeforeEach
    void setUp() {
        interceptor1 = mock(UpdateInterceptor.class);
        interceptor2 = mock(UpdateInterceptor.class);
        interceptor3 = mock(UpdateInterceptor.class);

        composite = new CompositeUpdateInterceptor(List.of(interceptor1, interceptor2, interceptor3));

        update = mock(Update.class);
    }

    @Test
    void shouldPassUpdateThroughAllInterceptorsWhenNoneReturnNull() {
        // Interceptors transform update in a chain
        Update u1 = mock(Update.class);
        Update u2 = mock(Update.class);

        when(interceptor1.intercept(update)).thenReturn(u1);
        when(interceptor2.intercept(u1)).thenReturn(u2);
        when(interceptor3.intercept(u2)).thenReturn(u2);

        Update result = composite.intercept(update);

        // Verify chained behavior
        verify(interceptor1).intercept(update);
        verify(interceptor2).intercept(u1);
        verify(interceptor3).intercept(u2);

        assertSame(u2, result, "The last interceptor's output should be returned");
    }

    @Test
    void shouldReturnNullWhenInterceptorReturnsNull() {
        // interceptor1 returns modified update
        Update u1 = mock(Update.class);

        when(interceptor1.intercept(update)).thenReturn(u1);
        // interceptor2 decides to skip update
        when(interceptor2.intercept(u1)).thenReturn(null);

        Update result = composite.intercept(update);

        // Verify call chain stops at interceptor2
        verify(interceptor1).intercept(update);
        verify(interceptor2).intercept(u1);
        verifyNoInteractions(interceptor3);

        assertNull(result, "Composite should return null when any interceptor returns null");
    }

    @Test
    void shouldReturnOriginalUpdateWhenNoInterceptorsProvided() {
        composite = new CompositeUpdateInterceptor(List.of());

        Update result = composite.intercept(update);

        assertSame(update, result, "With no interceptors, composite should return the input unchanged");
    }

    @Test
    void shouldPassNullToNextInterceptorsCorrectlyForLogging() {
        // Even though current becomes null, verify that logging path does not break execution

        when(interceptor1.intercept(update)).thenReturn(null);

        Update result = composite.intercept(update);

        verify(interceptor1).intercept(update);

        // Once null is returned, next interceptors must NOT be invoked
        verifyNoInteractions(interceptor2);
        verifyNoInteractions(interceptor3);

        assertNull(result);
    }
}
