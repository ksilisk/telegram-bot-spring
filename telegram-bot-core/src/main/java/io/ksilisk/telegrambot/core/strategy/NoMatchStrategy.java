package io.ksilisk.telegrambot.core.strategy;

import com.pengrad.telegrambot.model.Update;

public interface NoMatchStrategy {
    void handle(Update update);

    /// the name of the strategy, by default it returns the name of a strategy class
    /// this method is used by the strategySelector
    default String name() {
        return getClass().getSimpleName();
    }

    default boolean terminal() {
        return false;
    }
}
