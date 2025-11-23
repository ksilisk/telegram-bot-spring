package io.ksilisk.telegrambot.core.ingress;

import com.pengrad.telegrambot.model.Update;

/**
 * Specialization of {@link Ingress} for Telegram {@link Update} objects.
 * <p>
 * Implementations represent concrete sources of Telegram updates,
 * such as long polling, webhooks, message queues, or test feeds.
 * <p>
 * The lifecycle (start/stop) and actual delivery mechanics are
 * implementation-specific and are typically integrated with Spring
 * via appropriate mechanisms (e.g. {@code SmartLifecycle},
 * HTTP controllers, messaging listeners).
 */
public interface UpdateIngress extends Ingress<Update> {
}
