package io.ksilisk.telegrambot.core.selector;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.strategy.UpdateNoMatchStrategy;

/**
 * see {@link NoMatchStrategySelector}
 */
public interface UpdateNoMatchStrategySelector extends NoMatchStrategySelector<UpdateNoMatchStrategy, Update> {
}
