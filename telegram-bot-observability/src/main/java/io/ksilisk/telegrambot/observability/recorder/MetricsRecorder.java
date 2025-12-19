package io.ksilisk.telegrambot.observability.recorder;

import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.micrometer.core.instrument.Tags;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * Records observability metrics for the Telegram bot.
 *
 * <p>This interface provides a minimal abstraction over the underlying
 * metrics backend and is used by the observability module to record
 * counters, timers, summaries and gauges.</p>
 */
public interface MetricsRecorder {

    /**
     * Increment a counter metric by one.
     */
    void increment(TelegramBotMetric metric, Tags tags);

    /**
     * Record a duration in nanoseconds for a timer metric.
     */
    void recordTimer(TelegramBotMetric metric, Tags tags, long durationNanos);

    /**
     * Register a gauge backed by an {@link AtomicLong}.
     */
    void registerGauge(TelegramBotMetric metric, Tags tags, AtomicLong value);

    /**
     * Register a gauge backed by a value supplier.
     */
    void registerGauge(TelegramBotMetric metric, Tags tags, Supplier<Number> supplier);

    /**
     * Record a value for a summary metric.
     */
    void recordSummary(TelegramBotMetric metric, Tags tags, long amount);

    /**
     * Increment a counter metric by the given amount.
     *
     * <p>Implementations may override this method to provide a more efficient
     * backend-specific implementation.</p>
     */
    default void increment(TelegramBotMetric metric, Tags tags, long amount) {
        for (long i = 0; i < amount; i++) {
            increment(metric, tags);
        }
    }
}
