package io.ksilisk.telegrambot.core.rule;

import com.pengrad.telegrambot.model.InlineQuery;
import io.ksilisk.telegrambot.core.handler.update.inline.InlineUpdateHandler;

/**
 * Routing rule for {@link InlineQuery}-based updates.
 *
 * <p>Used by the routing layer to decide which {@link InlineUpdateHandler}
 * should handle a given inline query.</p>
 */
public interface InlineUpdateRule extends UpdateRule<InlineQuery> {
}
