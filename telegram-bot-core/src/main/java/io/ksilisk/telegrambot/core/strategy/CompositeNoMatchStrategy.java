package io.ksilisk.telegrambot.core.strategy;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.exception.strategy.StrategyExecutionException;
import io.ksilisk.telegrambot.core.selector.NoMatchStrategySelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CompositeNoMatchStrategy implements NoMatchStrategy {
    private static final Logger log = LoggerFactory.getLogger(CompositeNoMatchStrategy.class);

    private final List<NoMatchStrategy> noMatchStrategies;
    private final NoMatchStrategySelector noMatchStrategySelector;
    private final StrategyErrorPolicy strategyErrorPolicy;

    public CompositeNoMatchStrategy(List<NoMatchStrategy> noMatchStrategies,
                                    NoMatchStrategySelector noMatchStrategySelector,
                                    StrategyErrorPolicy strategyErrorPolicy) {
        this.noMatchStrategies = noMatchStrategies;
        this.noMatchStrategySelector = noMatchStrategySelector;
        this.strategyErrorPolicy = strategyErrorPolicy;
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
                    case LOG -> log.error("NoMatchStrategy '{}' failed", noMatchStrategy.name(), ex);
                }
            }
        }
    }

    @Override
    public String name() {
        return "composite";
    }
}
