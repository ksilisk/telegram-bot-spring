package io.ksilisk.telegrambot.core.ingress;

import com.pengrad.telegrambot.model.Update;

/**
 * An ingress for receiving Telegram {@link Update} objects via HTTP webhooks.
 * <p>
 * Implementations act as a bridge between an external HTTP endpoint
 * (typically a Spring MVC or WebFlux controller) and the internal update
 * delivery pipeline. The ingress itself is transport-agnostic: it does not
 * deal with HTTP details and is responsible only for forwarding updates
 * into the system.
 * <p>
 * The method {@link #handle(Update)} is expected to be non-blocking or
 * fast-returning. Any interruption or backpressure handling should be
 * performed inside the implementation.
 */
public interface WebhookUpdateIngress extends UpdateIngress {

    /**
     * Handles a single {@link Update} received from an external HTTP webhook.
     * <p>
     * Implementations should forward the update into the processing pipeline
     * (for example, through {@code UpdateDelivery}).
     *
     * @param update the Telegram update received from the webhook
     */
    void handle(Update update);
}
