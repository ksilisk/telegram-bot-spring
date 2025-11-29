package io.ksilisk.telegrambot.core.handler.update.inline;

import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.rule.InlineUpdateRule;

/**
 * {@link UpdateHandler} for inline query updates.
 *
 * <p>Inline handlers are selected by the routing layer using
 * {@link InlineUpdateRule} instances registered for the application.</p>
 */
public interface InlineUpdateHandler extends UpdateHandler {
}
