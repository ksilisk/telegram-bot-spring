package io.ksilisk.telegrambot.core.ingress;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.delivery.UpdateDelivery;

/**
 * Marker interface for components that act as an ingress point
 * for external events flowing into the bot.
 * <p>
 * An {@code Ingress} is a logical “source” of units of work ({@code U})
 * that are eventually delivered into the processing pipeline
 * (for example, to {@link UpdateDelivery} and routers).
 * <p>
 * This interface does not define any lifecycle or delivery methods on purpose:
 * concrete implementations are free to integrate with Spring's lifecycle
 * ({@code SmartLifecycle}, {@code @KafkaListener}, HTTP controllers, etc.)
 * in the way that best fits their transport.
 *
 * @param <U> type of unit produced by this ingress (e.g. {@link Update})
 */
public interface Ingress<U> {
}
