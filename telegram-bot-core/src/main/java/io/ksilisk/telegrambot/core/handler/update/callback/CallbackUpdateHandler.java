package io.ksilisk.telegrambot.core.handler.update.callback;

import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;

import java.util.Set;

/**
 * {@link UpdateHandler} for callback query updates.
 *
 * <p>Registered callback handlers are selected based on the callback data
 * returned from {@link #callbacks()}.</p>
 */
public interface CallbackUpdateHandler extends UpdateHandler {
    /**
     * Callback data values supported by this handler.
     *
     * <p>The routing layer uses these values to match incoming callback queries.</p>
     *
     * @return a non-empty set of supported callback identifiers
     */
    Set<String> callbacks();
}
