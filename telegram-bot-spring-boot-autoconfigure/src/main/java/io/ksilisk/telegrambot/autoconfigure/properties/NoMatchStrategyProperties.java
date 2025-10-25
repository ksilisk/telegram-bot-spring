package io.ksilisk.telegrambot.autoconfigure.properties;

import io.ksilisk.telegrambot.core.strategy.StrategyErrorPolicy;

public class NoMatchStrategyProperties {
    private static final StrategyErrorPolicy DEFAULT_STRATEGY_ERROR_POLICY = StrategyErrorPolicy.LOG;

    private StrategyErrorPolicy errorPolicy = DEFAULT_STRATEGY_ERROR_POLICY;

    public StrategyErrorPolicy getErrorPolicy() {
        return errorPolicy;
    }

    public void setErrorPolicy(StrategyErrorPolicy errorPolicy) {
        this.errorPolicy = errorPolicy;
    }
}
