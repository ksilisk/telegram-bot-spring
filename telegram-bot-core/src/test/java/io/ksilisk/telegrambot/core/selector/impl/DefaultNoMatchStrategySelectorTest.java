package io.ksilisk.telegrambot.core.selector.impl;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.strategy.NoMatchStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class DefaultNoMatchStrategySelectorTest {
    @Test
    void shouldReturnSameListInstance() {
        DefaultNoMatchStrategySelector selector = new DefaultNoMatchStrategySelector();

        NoMatchStrategy s1 = mock(NoMatchStrategy.class);
        NoMatchStrategy s2 = mock(NoMatchStrategy.class);

        List<NoMatchStrategy> strategies = List.of(s1, s2);

        Update update = mock(Update.class);

        List<NoMatchStrategy> result = selector.select(strategies, update);

        // Selector must return the exact same list instance
        assertSame(strategies, result, "Selector should return the original list unchanged");
    }

    @Test
    void shouldNotModifyOrReorderList() {
        DefaultNoMatchStrategySelector selector = new DefaultNoMatchStrategySelector();

        NoMatchStrategy s1 = mock(NoMatchStrategy.class);
        NoMatchStrategy s2 = mock(NoMatchStrategy.class);
        NoMatchStrategy s3 = mock(NoMatchStrategy.class);

        List<NoMatchStrategy> strategies = List.of(s1, s2, s3);

        List<NoMatchStrategy> result = selector.select(strategies, mock(Update.class));

        // Should not mutate, filter, or reorder elements
        assertEquals(3, result.size(), "Selector must not change the number of strategies");
        assertIterableEquals(strategies, result, "Selector must not reorder or modify list");
    }

    @Test
    void shouldReturnEmptyListAsIs() {
        DefaultNoMatchStrategySelector selector = new DefaultNoMatchStrategySelector();

        List<NoMatchStrategy> strategies = List.of();

        List<NoMatchStrategy> result = selector.select(strategies, mock(Update.class));

        assertTrue(result.isEmpty(), "Selector should return empty list unchanged");
        assertSame(strategies, result, "Empty list should be returned as the same instance");
    }
}
