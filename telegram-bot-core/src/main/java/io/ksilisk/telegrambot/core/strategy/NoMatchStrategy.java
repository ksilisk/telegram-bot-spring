package io.ksilisk.telegrambot.core.strategy;

import io.ksilisk.telegrambot.core.handler.update.Handler;
import io.ksilisk.telegrambot.core.selector.NoMatchStrategySelector;

/**
 * Handles updates for which no {@link Handler} was matched by the router.
 *
 * <p>A strategy may perform logging, metrics, fallback behavior or any other
 * custom action. Selection and ordering of strategies are determined by a
 * {@link NoMatchStrategySelector}.</p>
 *
 * @param <U> the type of value for strategy
 */
public interface NoMatchStrategy<U> {
    /**
     * Handle an update that was not matched by any handler.
     *
     * @param update the unmatched update, never {@code null}
     */
    void handle(U update);

    /**
     * A human-readable name for this strategy.
     * Defaults to the simple class name.
     */
    default String name() {
        return getClass().getSimpleName();
    }

    /**
     * Whether this strategy is terminal.
     *
     * <p>If {@code true}, selectors may stop selecting additional strategies
     * after this one.</p>
     */
    default boolean terminal() {
        return false;
    }
}
