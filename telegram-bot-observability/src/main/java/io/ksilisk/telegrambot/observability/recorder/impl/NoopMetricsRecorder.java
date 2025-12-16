package io.ksilisk.telegrambot.observability.recorder.impl;

import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import io.micrometer.core.instrument.Tags;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * No-op implementation of {@link MetricsRecorder}.
 *
 * <p>This recorder performs no metric recording and is used when
 * no metrics backend is configured.</p>
 */
public class NoopMetricsRecorder implements MetricsRecorder {
    @Override
    public void increment(TelegramBotMetric metric, Tags tags) {
        // noop
    }

    @Override
    public void recordTimer(TelegramBotMetric metric, Tags tags, long durationNanos) {
        // noop
    }

    @Override
    public void registerGauge(TelegramBotMetric metric, Tags tags, AtomicLong value) {
        // noop
    }

    @Override
    public void registerGauge(TelegramBotMetric metric, Tags tags, Supplier<Number> supplier) {
        // noop
    }

    @Override
    public void recordSummary(TelegramBotMetric metric, Tags tags, long amount) {
        // noop
    }
}
