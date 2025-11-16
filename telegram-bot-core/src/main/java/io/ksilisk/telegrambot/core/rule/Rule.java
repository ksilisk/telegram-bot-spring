package io.ksilisk.telegrambot.core.rule;

import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.matcher.Matcher;

/**
 * Defines a routing rule for a specific update type.
 *
 * <p>A rule consists of a {@link Matcher} used to determine whether the
 * rule applies to a given update, and an associated {@link UpdateHandler}
 * that will be invoked if the rule matches.</p>
 *
 * <p>Rules may be ordered; lower values have higher priority.</p>
 *
 * @param <U> the update payload type this rule matches on
 */
public interface Rule<U> {
    Matcher<U> matcher();

    UpdateHandler updateHandler();

    default int order() {
        return 0;
    }
}
