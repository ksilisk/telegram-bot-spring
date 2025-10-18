package io.ksilisk.telegrambot.core.registry.rule;

import io.ksilisk.telegrambot.core.registry.Registry;
import io.ksilisk.telegrambot.core.rule.Rule;

/// used for types of handlers that need a rule to be invoked
public interface RuleRegistry<S extends Rule<K>, K> extends Registry<S, K> {
}
