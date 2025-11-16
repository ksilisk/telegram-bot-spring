package io.ksilisk.telegrambot.core.strategy;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.selector.NoMatchStrategySelector;

/**
 * Handles updates for which no {@link UpdateHandler} was matched by the router.
 *
 * <p>A strategy may perform logging, metrics, fallback behavior or any other
 * custom action. Selection and ordering of strategies are determined by a
 * {@link NoMatchStrategySelector}.</p>
 */
public interface NoMatchStrategy {
    /**
     * Handle an update that was not matched by any handler.
     *
     * @param update the unmatched update, never {@code null}
     */
    void handle(Update update);

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
