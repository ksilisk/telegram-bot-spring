package io.ksilisk.telegrambot.core.selector.impl;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.selector.UpdateNoMatchStrategySelector;
import io.ksilisk.telegrambot.core.strategy.UpdateNoMatchStrategy;

import java.util.List;

public class DefaultNoMatchStrategySelector implements UpdateNoMatchStrategySelector {
    @Override
    public List<UpdateNoMatchStrategy> select(List<UpdateNoMatchStrategy> noMatchStrategies, Update update) {
        return noMatchStrategies;
    }
}
