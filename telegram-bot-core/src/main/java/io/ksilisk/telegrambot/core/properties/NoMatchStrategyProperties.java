package io.ksilisk.telegrambot.core.properties;

import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.strategy.StrategyErrorPolicy;

/**
 * Configuration properties for handling updates that do not match
 * any registered {@link UpdateHandler}.
 *
 * <p>Controls how errors thrown inside {@code NoMatchStrategy} implementations
 * are handled by the framework.</p>
 */
public class NoMatchStrategyProperties {
    private static final StrategyErrorPolicy DEFAULT_STRATEGY_ERROR_POLICY = StrategyErrorPolicy.LOG;

    /**
     * Error handling policy applied when a {@code NoMatchStrategy} throws
     * an exception.
     *
     * <p>Defaults to {@code LOG}.</p>
     */
    private StrategyErrorPolicy errorPolicy = DEFAULT_STRATEGY_ERROR_POLICY;

    public StrategyErrorPolicy getErrorPolicy() {
        return errorPolicy;
    }

    public void setErrorPolicy(StrategyErrorPolicy errorPolicy) {
        this.errorPolicy = errorPolicy;
    }
}
