package io.ksilisk.telegrambot.core.selector;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.strategy.NoMatchStrategy;

import java.util.List;

@FunctionalInterface
public interface NoMatchStrategySelector {
    List<NoMatchStrategy> select(List<NoMatchStrategy> noMatchStrategies, Update update);
}
