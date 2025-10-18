package io.ksilisk.telegrambot.core.rule;

public interface RuleSupplier<U> {
    Rule<U> rule();
}
