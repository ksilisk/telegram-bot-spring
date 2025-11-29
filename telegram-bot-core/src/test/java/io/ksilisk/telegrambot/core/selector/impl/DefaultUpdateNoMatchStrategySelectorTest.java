package io.ksilisk.telegrambot.core.selector.impl;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.strategy.UpdateNoMatchStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class DefaultUpdateNoMatchStrategySelectorTest {
    @Test
    void shouldReturnSameListInstance() {
        DefaultNoMatchStrategySelector selector = new DefaultNoMatchStrategySelector();

        UpdateNoMatchStrategy s1 = mock(UpdateNoMatchStrategy.class);
        UpdateNoMatchStrategy s2 = mock(UpdateNoMatchStrategy.class);

        List<UpdateNoMatchStrategy> strategies = List.of(s1, s2);

        Update update = mock(Update.class);

        List<UpdateNoMatchStrategy> result = selector.select(strategies, update);

        // Selector must return the exact same list instance
        assertSame(strategies, result, "Selector should return the original list unchanged");
    }

    @Test
    void shouldNotModifyOrReorderList() {
        DefaultNoMatchStrategySelector selector = new DefaultNoMatchStrategySelector();

        UpdateNoMatchStrategy s1 = mock(UpdateNoMatchStrategy.class);
        UpdateNoMatchStrategy s2 = mock(UpdateNoMatchStrategy.class);
        UpdateNoMatchStrategy s3 = mock(UpdateNoMatchStrategy.class);

        List<UpdateNoMatchStrategy> strategies = List.of(s1, s2, s3);

        List<UpdateNoMatchStrategy> result = selector.select(strategies, mock(Update.class));

        // Should not mutate, filter, or reorder elements
        assertEquals(3, result.size(), "Selector must not change the number of strategies");
        assertIterableEquals(strategies, result, "Selector must not reorder or modify list");
    }

    @Test
    void shouldReturnEmptyListAsIs() {
        DefaultNoMatchStrategySelector selector = new DefaultNoMatchStrategySelector();

        List<UpdateNoMatchStrategy> strategies = List.of();

        List<UpdateNoMatchStrategy> result = selector.select(strategies, mock(Update.class));

        assertTrue(result.isEmpty(), "Selector should return empty list unchanged");
        assertSame(strategies, result, "Empty list should be returned as the same instance");
    }
}
