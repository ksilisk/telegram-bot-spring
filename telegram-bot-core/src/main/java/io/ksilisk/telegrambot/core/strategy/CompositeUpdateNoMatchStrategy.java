package io.ksilisk.telegrambot.core.strategy;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.exception.strategy.StrategyExecutionException;
import io.ksilisk.telegrambot.core.selector.UpdateNoMatchStrategySelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CompositeUpdateNoMatchStrategy implements UpdateNoMatchStrategy {
    private static final Logger log = LoggerFactory.getLogger(CompositeUpdateNoMatchStrategy.class);

    private final List<UpdateNoMatchStrategy> noMatchStrategies;
    private final UpdateNoMatchStrategySelector noMatchStrategySelector;
    private final StrategyErrorPolicy strategyErrorPolicy;

    public CompositeUpdateNoMatchStrategy(List<UpdateNoMatchStrategy> noMatchStrategies,
                                          UpdateNoMatchStrategySelector noMatchStrategySelector,
                                          StrategyErrorPolicy strategyErrorPolicy) {
        this.noMatchStrategies = noMatchStrategies;
        this.noMatchStrategySelector = noMatchStrategySelector;
        this.strategyErrorPolicy = strategyErrorPolicy;
    }

    @Override
    public void handle(Update update) {
        List<UpdateNoMatchStrategy> selectedStrategies = noMatchStrategySelector.select(this.noMatchStrategies, update);
        for (UpdateNoMatchStrategy updateNoMatchStrategy : selectedStrategies) {
            try {
                log.debug("Invoking NoMatchStrategy: '{}'", updateNoMatchStrategy.name());
                updateNoMatchStrategy.handle(update);
                if (updateNoMatchStrategy.terminal()) {
                    return;
                }
            } catch (Exception ex) {
                switch (strategyErrorPolicy) {
                    case THROW -> throw new StrategyExecutionException(ex);
                    case LOG -> log.warn(
                            "NoMatchStrategy '{}' failed while handling update (id={}). " +
                                    "Ignoring due to ErrorPolicy=LOG",
                            updateNoMatchStrategy.name(), update.updateId(), ex);
                }
            }
        }
    }

    @Override
    public String name() {
        return "composite";
    }
}
