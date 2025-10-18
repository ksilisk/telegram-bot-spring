package io.ksilisk.telegrambot.core.rule;

import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.matcher.Matcher;

/// a rule that is used to match an update with a handler
/// you can also set an order for a particular Rule
public interface Rule<U> {
    Matcher<U> matcher();

    UpdateHandler updateHandler();

    default int order() {
        return 0;
    }
}
