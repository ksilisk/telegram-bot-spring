package io.ksilisk.telegrambot.core.selector.impl;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.selector.NoMatchStrategySelector;
import io.ksilisk.telegrambot.core.strategy.NoMatchStrategy;

import java.util.List;

public class DefaultNoMatchStrategySelector implements NoMatchStrategySelector {
    @Override
    public List<NoMatchStrategy> select(List<NoMatchStrategy> noMatchStrategies, Update update) {
        return noMatchStrategies;
    }
}
