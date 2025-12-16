package io.ksilisk.telegrambot.observability.recorder.impl;

import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import io.micrometer.core.instrument.Tags;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class NoopMetricsRecorderTest {
    private final MetricsRecorder recorder = new NoopMetricsRecorder();

    @Test
    void shouldNotThrowOnAllOperations() {
        Tags tags = Tags.of("k", "v");

        assertDoesNotThrow(() -> recorder.increment(TelegramBotMetric.API_CALLS, tags));
        assertDoesNotThrow(() -> recorder.increment(TelegramBotMetric.UPDATES_RECEIVED, tags, 5L));

        assertDoesNotThrow(() -> recorder.recordTimer(TelegramBotMetric.API_DURATION, tags, 123_456L));

        assertDoesNotThrow(() -> recorder.registerGauge(
                TelegramBotMetric.DELIVERY_POOL_QUEUE_SIZE,
                tags,
                new AtomicLong(42L)
        ));

        assertDoesNotThrow(() -> recorder.registerGauge(
                TelegramBotMetric.DELIVERY_POOL_QUEUE_SIZE,
                tags,
                () -> 7
        ));

        assertDoesNotThrow(() -> recorder.recordSummary(TelegramBotMetric.UPDATES_BATCH_SIZE, tags, 3L));
    }

    @Test
    void shouldNotEvaluateGaugeSupplier() {
        Tags tags = Tags.empty();

        AtomicReference<String> touched = new AtomicReference<>(null);
        Supplier<Number> supplier = () -> {
            touched.set("called");
            return 1;
        };

        assertDoesNotThrow(() -> recorder.registerGauge(
                TelegramBotMetric.DELIVERY_POOL_ACTIVE,
                tags,
                supplier
        ));

        // Noop recorder should not call the supplier during registration.
        assertNull(touched.get());
    }
}
