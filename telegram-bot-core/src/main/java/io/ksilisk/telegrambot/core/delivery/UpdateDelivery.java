package io.ksilisk.telegrambot.core.delivery;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.poller.UpdatePoller;

import java.io.Closeable;
import java.util.List;

/**
 * Delivers batches of polled {@link Update Updates} to the next stage
 * of the processing pipeline (e.g. a dispatcher).
 *
 * <p>{@link #close()} indicates that no further deliveries will occur
 * and any internal resources should be released.</p>
 *
 * @see UpdatePoller
 */
public interface UpdateDelivery extends Closeable {
    /**
     * Deliver a batch of updates.
     *
     * @param polledUpdates never {@code null}
     */
    void deliver(List<Update> polledUpdates);
}
