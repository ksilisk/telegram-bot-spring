package io.ksilisk.telegrambot.core.handler.update.message;

import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.rule.MessageUpdateRule;

/**
 * {@link UpdateHandler} for regular message updates.
 *
 * <p>Message handlers are selected by the routing layer using
 * {@link MessageUpdateRule} instances registered for the application.</p>
 */
public interface MessageUpdateHandler extends UpdateHandler {
}
