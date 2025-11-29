package io.ksilisk.telegrambot.core.router;

import io.ksilisk.telegrambot.core.handler.update.Handler;

/**
 * Routes an update to an appropriate {@link Handler}.
 *
 * <p>The router first determines whether it can process the update via
 * {@link #supports(U)}. If supported, {@link #route(U)} attempts to
 * resolve and invoke a matching handler.</p>
 *
 * <p>Concrete implementations typically use one or more {@code Registry}
 * instances (e.g. command, message, callback, or inline registries) to
 * perform handler lookup.</p>
 *
 * @param <U> the type of value being routed
 */
public interface Router<U> {
    /**
     * Whether this router can process the given update.
     *
     * @param update never {@code null}
     * @return {@code true} if this router is applicable
     */
    boolean supports(U update);

    /**
     * Route the update to an appropriate handler.
     *
     * @param update never {@code null}
     * @return {@code true} if a handler was found and invoked
     */
    boolean route(U update);
}
