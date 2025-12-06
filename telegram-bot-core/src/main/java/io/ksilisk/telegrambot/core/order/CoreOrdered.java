package io.ksilisk.telegrambot.core.order;

import java.util.Comparator;

/**
 * Common contract for components that participate in ordered processing.
 * <p>
 * Implementations may override {@link #getOrder()} to define their priority.
 * Lower values represent higher precedence.
 * <p>
 * This interface is fully framework-agnostic and does not rely on Spring.
 */
public interface CoreOrdered {
    Comparator<CoreOrdered> COMPARATOR = Comparator.comparingInt(CoreOrdered::getOrder);

    /**
     * Order of this component.
     * Lower values indicate higher priority.
     *
     * @return the component order; default is {@code 0}
     */
    default int getOrder() {
        return 0;
    }
}
