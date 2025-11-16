package io.ksilisk.telegrambot.core.registry.rule;

import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.registry.Registry;
import io.ksilisk.telegrambot.core.rule.Rule;

/**
 * Registry for routing rules.
 *
 * <p>Used by the routing layer to resolve the appropriate
 * {@link UpdateHandler} based on {@link Rule} matching.</p>
 *
 * @param <S> the rule type
 * @param <K> the update payload type
 */
public interface RuleRegistry<S extends Rule<K>, K> extends Registry<S, K> {
}
