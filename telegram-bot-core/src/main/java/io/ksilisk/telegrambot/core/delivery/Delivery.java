package io.ksilisk.telegrambot.core.delivery;

import java.util.List;

/**
 * Marker interface for delivery components in the processing pipeline.
 *
 * <p>Defines the generic contract for components responsible for forwarding
 * values to the next stage of processing. Specific delivery mechanisms
 * (such as update delivery) extend this interface with concrete methods.</p>
 *
 * @param <U> the type of value being delivered
 */
public interface Delivery<U> {
    /**
     * Submit a batch of values for delivery.
     *
     * @param updates the values to deliver, never {@code null}
     */
    void deliver(List<U> updates);

    /**
     * Initialize and start the delivery infrastructure
     * (e.g. thread pools or internal queues).
     *
     * <p>Multiple invocations should be safe and have no additional effect.</p>
     */
    void start();

    /**
     * Stop the delivery infrastructure and release any allocated resources.
     *
     * <p>Subsequent calls should be safe and have no effect.</p>
     */
    void stop();
}
