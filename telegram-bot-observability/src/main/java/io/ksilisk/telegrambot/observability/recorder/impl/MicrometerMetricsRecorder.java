package io.ksilisk.telegrambot.observability.recorder.impl;

import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * {@link MetricsRecorder} implementation backed by Micrometer.
 *
 * <p>Metrics are recorded using a {@link io.micrometer.core.instrument.MeterRegistry}
 * and exported via the configured Micrometer backend.</p>
 */
public class MicrometerMetricsRecorder implements MetricsRecorder {
    private final MeterRegistry meterRegistry;

    private final ConcurrentHashMap<MeterKey, Counter> counters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<MeterKey, Timer> timers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<MeterKey, Boolean> gauges = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<MeterKey, DistributionSummary> summaries = new ConcurrentHashMap<>();

    public MicrometerMetricsRecorder(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void increment(TelegramBotMetric metric, Tags tags) {
        counter(metric, normalize(tags)).increment();
    }

    @Override
    public void increment(TelegramBotMetric metric, Tags tags, long amount) {
        counter(metric, tags).increment(amount);
    }

    private Counter counter(TelegramBotMetric metric, Tags tags) {
        MeterKey key = new MeterKey(metric.metricName(), tags);
        return counters.computeIfAbsent(
                key,
                k -> Counter.builder(k.name)
                        .tags(k.tags)
                        .description(metric.description())
                        .register(meterRegistry)
        );
    }

    @Override
    public void recordTimer(TelegramBotMetric metric, Tags tags, long durationNanos) {
        timer(metric, normalize(tags))
                .record(Duration.ofNanos(durationNanos));
    }

    private Timer timer(TelegramBotMetric metric, Tags tags) {
        MeterKey key = new MeterKey(metric.metricName(), tags);
        return timers.computeIfAbsent(
                key,
                k -> Timer.builder(k.name)
                        .tags(k.tags)
                        .description(metric.description())
                        .register(meterRegistry)
        );
    }

    @Override
    public void registerGauge(TelegramBotMetric metric, Tags tags, AtomicLong value) {
        MeterKey key = new MeterKey(metric.metricName(), normalize(tags));

        gauges.computeIfAbsent(key, k -> {
            Gauge.builder(k.name, value, AtomicLong::get)
                    .tags(k.tags)
                    .description(metric.description())
                    .register(meterRegistry);
            return true;
        });
    }

    @Override
    public void registerGauge(TelegramBotMetric metric, Tags tags, Supplier<Number> supplier) {
        Gauge.builder(metric.metricName(), supplier)
                .tags(normalize(tags))
                .description(metric.description())
                .register(meterRegistry);
    }

    @Override
    public void recordSummary(TelegramBotMetric metric, Tags tags, long amount) {
        DistributionSummary s = summaries.computeIfAbsent(new MeterKey(metric.metricName(), tags), k ->
                DistributionSummary.builder(metric.metricName())
                        .description(metric.description())
                        .tags(tags)
                        .register(meterRegistry)
        );
        s.record(amount);
    }

    private static Tags normalize(Tags tags) {
        if (tags == null || tags.stream().findAny().isEmpty()) {
            return Tags.empty();
        }

        List<Tag> list = new ArrayList<>();
        tags.forEach(list::add);

        list.sort(Comparator
                .comparing(Tag::getKey)
                .thenComparing(Tag::getValue));

        return Tags.of(list);
    }

    private record MeterKey(String name, Tags tags) {
    }
}
