package io.ksilisk.telegrambot.core.delivery;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Factory for creating the executor used by the delivery pipeline.
 *
 * <p>Implementations may customize thread count, queue type, rejection policy
 * or thread naming. The returned executor must be fully initialized and ready
 * for use. Spring applications may return any {@link Executor}, such as a
 * {@code ThreadPoolTaskExecutor}; the default implementation returns a
 * {@link ThreadPoolExecutor}.</p>
 */
public interface DeliveryThreadPoolExecutorFactory {
    /**
     * Build a new executor instance.
     *
     * @return a configured executor, never {@code null}
     */
    Executor buildThreadPoolExecutor();
}
