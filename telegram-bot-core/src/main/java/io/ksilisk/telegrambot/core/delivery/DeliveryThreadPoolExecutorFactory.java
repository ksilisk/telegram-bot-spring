package io.ksilisk.telegrambot.core.delivery;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Factory for creating the {@link ThreadPoolExecutor} used by the delivery pipeline.
 *
 * <p>Implementations may customize thread count, queue type, rejection policy
 * or thread naming. The returned executor must be fully initialized and ready
 * for use.</p>
 */
public interface DeliveryThreadPoolExecutorFactory {
    /**
     * Build a new {@link ThreadPoolExecutor} instance.
     *
     * @return a configured executor, never {@code null}
     */
    ThreadPoolExecutor buildThreadPoolExecutor();
}
