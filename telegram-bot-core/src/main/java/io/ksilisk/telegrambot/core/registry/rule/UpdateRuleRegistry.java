package io.ksilisk.telegrambot.core.registry.rule;

import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.registry.UpdateRegistry;
import io.ksilisk.telegrambot.core.rule.Rule;
import io.ksilisk.telegrambot.core.rule.UpdateRule;

/**
 * Registry for routing rules.
 *
 * <p>Used by the routing layer to resolve the appropriate
 * {@link UpdateHandler} based on {@link Rule} matching.</p>
 *
 * @param <S> the rule type
 * @param <K> the update payload type
 */
public interface UpdateRuleRegistry<S extends UpdateRule<K>, K> extends UpdateRegistry<K> {
}
