package io.ksilisk.telegrambot.core.selector;

import io.ksilisk.telegrambot.core.strategy.NoMatchStrategy;

import java.util.List;

/**
 * Selects the {@link NoMatchStrategy NoMatchStrategies} that should handle
 * an update for which no handler was matched.
 *
 * <p>The selector controls which strategies will be invoked and in what
 * order. It may also consider {@link NoMatchStrategy#terminal()} to stop
 * further selection.</p>
 *
 * @param <U> the type of value being selected
 * @param <N> the type of strategies
 */
@FunctionalInterface
public interface NoMatchStrategySelector<N extends NoMatchStrategy<U>, U> {
    /**
     * Select strategies for the given update.
     *
     * @param noMatchStrategies all available strategies, never {@code null}
     * @param update            the unmatched update, never {@code null}
     * @return ordered strategies to apply, never {@code null}
     */
    List<N> select(List<N> noMatchStrategies, U update);
}
