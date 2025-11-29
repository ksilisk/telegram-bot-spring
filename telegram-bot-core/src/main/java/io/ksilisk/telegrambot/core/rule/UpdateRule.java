package io.ksilisk.telegrambot.core.rule;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;

/**
 * see {@link Rule}
 */
public interface UpdateRule<K> extends Rule<K, UpdateHandler, Update> {
}
