package io.ksilisk.telegrambot.core.rule;

import com.pengrad.telegrambot.model.Message;
import io.ksilisk.telegrambot.core.handler.update.message.MessageUpdateHandler;

/**
 * Routing rule for {@link Message}-based updates.
 *
 * <p>Used by the routing layer to select appropriate {@link MessageUpdateHandler}
 * instances for a given message.</p>
 */
public interface MessageRule extends Rule<Message>{
}
