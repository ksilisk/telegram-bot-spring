package io.ksilisk.telegrambot.core.router;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;

/**
 * Routes a Telegram {@link Update} to an appropriate {@link UpdateHandler}.
 *
 * <p>The router first determines whether it can process the update via
 * {@link #supports(Update)}. If supported, {@link #route(Update)} attempts to
 * resolve and invoke a matching handler.</p>
 *
 * <p>Concrete implementations typically use one or more {@code Registry}
 * instances (e.g. command, message, callback, or inline registries) to
 * perform handler lookup.</p>
 */
public interface UpdateRouter {
    /**
     * Whether this router can process the given update.
     *
     * @param update never {@code null}
     * @return {@code true} if this router is applicable
     */
    boolean supports(Update update);

    /**
     * Route the update to an appropriate handler.
     *
     * @param update never {@code null}
     * @return {@code true} if a handler was found and invoked
     */
    boolean route(Update update);
}
