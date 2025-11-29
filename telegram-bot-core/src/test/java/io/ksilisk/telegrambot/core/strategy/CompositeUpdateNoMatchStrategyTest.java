package io.ksilisk.telegrambot.core.strategy;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.exception.strategy.StrategyExecutionException;
import io.ksilisk.telegrambot.core.selector.UpdateNoMatchStrategySelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CompositeUpdateNoMatchStrategyTest {
    private UpdateNoMatchStrategy strategy1;
    private UpdateNoMatchStrategy strategy2;
    private UpdateNoMatchStrategySelector selector;
    private CompositeUpdateNoMatchStrategy composite;
    private Update update;

    @BeforeEach
    void setUp() {
        strategy1 = mock(UpdateNoMatchStrategy.class);
        strategy2 = mock(UpdateNoMatchStrategy.class);
        selector = mock(UpdateNoMatchStrategySelector.class);
        update = mock(Update.class);

        composite = new CompositeUpdateNoMatchStrategy(
                List.of(strategy1, strategy2),
                selector,
                StrategyErrorPolicy.LOG // default, can be overridden in specific tests
        );
    }

    @Test
    void shouldInvokeStrategiesInSelectedOrderAndRespectTerminalFlag() {
        // Selector returns both strategies in order
        when(selector.select(List.of(strategy1, strategy2), update))
                .thenReturn(List.of(strategy1, strategy2));

        // First strategy is non-terminal
        when(strategy1.terminal()).thenReturn(false);
        // Second strategy is terminal
        when(strategy2.terminal()).thenReturn(true);

        composite.handle(update);

        // Selector must be invoked with original list
        verify(selector).select(List.of(strategy1, strategy2), update);

        // Both strategies should be called
        verify(strategy1).handle(update);
        verify(strategy1).terminal();

        verify(strategy2).handle(update);
        verify(strategy2).terminal();
    }

    @Test
    void shouldStopProcessingWhenStrategyIsTerminal() {
        when(selector.select(List.of(strategy1, strategy2), update))
                .thenReturn(List.of(strategy1, strategy2));

        // First strategy is terminal
        when(strategy1.terminal()).thenReturn(true);

        composite.handle(update);

        verify(strategy1).handle(update);
        verify(strategy1).terminal();

        // Once a terminal strategy has been executed, next strategies must not be invoked
        verifyNoInteractions(strategy2);
    }

    @Test
    void shouldContinueOnExceptionWhenPolicyIsLog() {
        composite = new CompositeUpdateNoMatchStrategy(
                List.of(strategy1, strategy2),
                selector,
                StrategyErrorPolicy.LOG
        );

        when(selector.select(List.of(strategy1, strategy2), update))
                .thenReturn(List.of(strategy1, strategy2));

        // First strategy throws an exception
        doThrow(new RuntimeException("failure"))
                .when(strategy1).handle(update);

        when(strategy1.terminal()).thenReturn(false);
        when(strategy2.terminal()).thenReturn(false);

        // With LOG policy, exception is swallowed and processing continues
        composite.handle(update);

        verify(strategy1).handle(update);
        // terminal() may or may not be called depending on implementation, but we don't rely on it here

        // Second strategy should still be executed
        verify(strategy2).handle(update);
    }

    @Test
    void shouldThrowStrategyExecutionExceptionWhenPolicyIsThrow() {
        composite = new CompositeUpdateNoMatchStrategy(
                List.of(strategy1, strategy2),
                selector,
                StrategyErrorPolicy.THROW
        );

        when(selector.select(List.of(strategy1, strategy2), update))
                .thenReturn(List.of(strategy1, strategy2));

        doThrow(new RuntimeException("failure"))
                .when(strategy1).handle(update);

        assertThrows(
                StrategyExecutionException.class,
                () -> composite.handle(update),
                "THROW policy should wrap underlying exception into StrategyExecutionException"
        );

        // Once exception is thrown, next strategies must not be invoked
        verifyNoInteractions(strategy2);
    }

    @Test
    void shouldHaveCompositeName() {
        assertEquals("composite", composite.name(), "Composite strategy must have fixed name 'composite'");
    }
}
