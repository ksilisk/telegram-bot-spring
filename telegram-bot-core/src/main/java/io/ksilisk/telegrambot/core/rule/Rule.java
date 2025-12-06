package io.ksilisk.telegrambot.core.rule;

import io.ksilisk.telegrambot.core.handler.update.Handler;
import io.ksilisk.telegrambot.core.matcher.Matcher;
import io.ksilisk.telegrambot.core.order.CoreOrdered;

/**
 * Defines a routing rule for a specific update type.
 *
 * <p>A rule consists of a {@link Matcher} used to determine whether the
 * rule applies to a given update, and an associated {@link Handler}
 * that will be invoked if the rule matches.</p>
 *
 * <p>Rules may be ordered; lower values have higher priority.</p>
 *
 * @param <K> the update payload type this rule matches on
 * @param <H> the handler implementation type this rule returns
 * @param <U> the type of value being ruled
 */
public interface Rule<K, H extends Handler<U>, U> extends CoreOrdered {
    Matcher<K> matcher();

    H handler();
}
