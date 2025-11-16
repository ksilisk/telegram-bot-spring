package io.ksilisk.telegrambot.core.matcher;

/**
 * Predicate-like component used to determine whether a routing rule
 * applies to a given update payload.
 *
 * @param <U> the update payload type this matcher evaluates
 */
@FunctionalInterface
public interface Matcher<U> {
    /**
     * Evaluate whether this matcher applies to the given update payload.
     *
     * @param update the update payload to evaluate, never {@code null}
     * @return {@code true} if the rule should match
     */
    boolean match(U update);
}
