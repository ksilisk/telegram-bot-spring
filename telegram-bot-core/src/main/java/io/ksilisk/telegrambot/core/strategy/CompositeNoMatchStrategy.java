package io.ksilisk.telegrambot.core.strategy;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.exception.strategy.StrategyExecutionException;
import io.ksilisk.telegrambot.core.logger.BotLogger;
import io.ksilisk.telegrambot.core.selector.NoMatchStrategySelector;

import java.util.List;

public class CompositeNoMatchStrategy implements NoMatchStrategy {
    private final List<NoMatchStrategy> noMatchStrategies;
    private final NoMatchStrategySelector noMatchStrategySelector;
    private final StrategyErrorPolicy strategyErrorPolicy;
    private final BotLogger log;

    public CompositeNoMatchStrategy(List<NoMatchStrategy> noMatchStrategies,
                                    NoMatchStrategySelector noMatchStrategySelector,
                                    StrategyErrorPolicy strategyErrorPolicy, BotLogger botLogger) {
        this.noMatchStrategies = noMatchStrategies;
        this.noMatchStrategySelector = noMatchStrategySelector;
        this.strategyErrorPolicy = strategyErrorPolicy;
        this.log = botLogger;
    }

    public CompositeNoMatchStrategy(List<NoMatchStrategy> noMatchStrategies,
                                    NoMatchStrategySelector noMatchStrategySelector,
                                    StrategyErrorPolicy strategyErrorPolicy) {
        this(noMatchStrategies, noMatchStrategySelector, strategyErrorPolicy, BotLogger.NO_OP);
    }

    @Override
    public void handle(Update update) {
        List<NoMatchStrategy> selectedStrategies = noMatchStrategySelector.select(this.noMatchStrategies, update);
        for (NoMatchStrategy noMatchStrategy : selectedStrategies) {
            try {
                noMatchStrategy.handle(update);
                if (noMatchStrategy.terminal()) {
                    return;
                }
            } catch (Exception ex) {
                switch (strategyErrorPolicy) {
                    case THROW -> throw new StrategyExecutionException(ex);
                    case LOG -> log.error("NoMatchStrategy '{}' failed", ex, noMatchStrategy.name());
                }
            }
        }
    }

    @Override
    public String name() {
        return "composite";
    }
}
