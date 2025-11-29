package io.ksilisk.telegrambot.core.delivery;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.dispatcher.UpdateDispatcher;
import io.ksilisk.telegrambot.core.poller.UpdatePoller;


/**
 * Delivers batches of polled {@link Update Updates} to the next stage of the
 * processing pipeline (such as dispatching).
 *
 * <p>Implementations are typically backed by a thread pool and may invoke the
 * downstream stages concurrently. Therefore, delivery components and the
 * handlers they invoke must be thread-safe.</p>
 *
 * @see UpdatePoller
 * @see UpdateDispatcher
 */
public interface UpdateDelivery extends Delivery<Update> {
}
