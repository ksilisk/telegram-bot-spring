package io.ksilisk.telegrambot.observability.recorder.impl;

import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MicrometerMetricsRecorderTest {
    private SimpleMeterRegistry registry;
    private MetricsRecorder recorder;

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
        recorder = new MicrometerMetricsRecorder(registry);
    }

    @Test
    void increment_shouldCreateAndIncrementCounter() {
        Tags tags = Tags.of("method", "SendMessage", "status", "success");

        recorder.increment(TelegramBotMetric.API_CALLS, tags);
        recorder.increment(TelegramBotMetric.API_CALLS, tags);

        Counter c = registry.get(TelegramBotMetric.API_CALLS.metricName()).tags(tags).counter();
        assertEquals(2.0, c.count(), 0.000001);

        assertEquals(TelegramBotMetric.API_CALLS.description(), c.getId().getDescription());
    }

    @Test
    void increment_withAmount_shouldAddAmount() {
        Tags tags = Tags.of("k", "v");

        recorder.increment(TelegramBotMetric.UPDATES_RECEIVED, tags, 5L);

        Counter c = registry.get(TelegramBotMetric.UPDATES_RECEIVED.metricName()).tags(tags).counter();
        assertEquals(5.0, c.count(), 0.000001);
        assertEquals(TelegramBotMetric.UPDATES_RECEIVED.description(), c.getId().getDescription());
    }

    @Test
    void recordTimer_shouldRecordNanos() {
        Tags tags = Tags.of("handler", "StartCommandUpdateHandler", "status", "success");

        long nanos = 123_456_789L;
        recorder.recordTimer(TelegramBotMetric.HANDLER_DURATION, tags, nanos);

        Timer t = registry.get(TelegramBotMetric.HANDLER_DURATION.metricName()).tags(tags).timer();
        assertEquals(1L, t.count());
        assertEquals((double) nanos, t.totalTime(TimeUnit.NANOSECONDS), 0.000001);

        assertEquals(TelegramBotMetric.HANDLER_DURATION.description(), t.getId().getDescription());
    }

    @Test
    void recordSummary_shouldRecordAmount() {
        Tags tags = Tags.empty();

        recorder.recordSummary(TelegramBotMetric.UPDATES_BATCH_SIZE, tags, 3L);
        recorder.recordSummary(TelegramBotMetric.UPDATES_BATCH_SIZE, tags, 7L);

        DistributionSummary s = registry.get(TelegramBotMetric.UPDATES_BATCH_SIZE.metricName()).tags(tags).summary();
        assertEquals(2L, s.count());
        assertEquals(10.0, s.totalAmount(), 0.000001);

        assertEquals(TelegramBotMetric.UPDATES_BATCH_SIZE.description(), s.getId().getDescription());
    }

    @Test
    void registerGauge_withSupplier_shouldReadSupplierValue() {
        Tags tags = Tags.of("pool", "delivery");

        AtomicLong value = new AtomicLong(42L);
        recorder.registerGauge(TelegramBotMetric.DELIVERY_POOL_QUEUE_SIZE, tags, value::get);

        Gauge g = registry.get(TelegramBotMetric.DELIVERY_POOL_QUEUE_SIZE.metricName()).tags(tags).gauge();
        assertNotNull(g);
        assertEquals(42.0, g.value(), 0.000001);

        value.set(100L);
        assertEquals(100.0, g.value(), 0.000001);

        assertEquals(TelegramBotMetric.DELIVERY_POOL_QUEUE_SIZE.description(), g.getId().getDescription());
    }

    @Test
    void registerGauge_withAtomicLong_shouldReadAtomicValue() {
        Tags tags = Tags.empty();

        AtomicLong value = new AtomicLong(5L);
        recorder.registerGauge(TelegramBotMetric.DELIVERY_POOL_ACTIVE, tags, value);

        Gauge g = registry.get(TelegramBotMetric.DELIVERY_POOL_ACTIVE.metricName()).tags(tags).gauge();
        assertEquals(5.0, g.value(), 0.000001);

        value.set(9L);
        assertEquals(9.0, g.value(), 0.000001);

        assertEquals(TelegramBotMetric.DELIVERY_POOL_ACTIVE.description(), g.getId().getDescription());
    }
}
