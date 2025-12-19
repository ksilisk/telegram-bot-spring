package io.ksilisk.telegrambot.observability.handler.exception;

import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import io.ksilisk.telegrambot.observability.recorder.impl.MicrometerMetricsRecorder;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.core.Ordered;

import static org.junit.jupiter.api.Assertions.*;

class ObservabilityUpdateExceptionHandlerTest {
    @Test
    void supports_shouldAlwaysReturnTrue() {
        MetricsRecorder recorder = new MicrometerMetricsRecorder(new SimpleMeterRegistry());
        ObservabilityUpdateExceptionHandler handler =
                new ObservabilityUpdateExceptionHandler(recorder);

        assertTrue(handler.supports(new RuntimeException(), null));
    }

    @Test
    void handle_shouldIncrementExceptionsTotalCounter() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        MetricsRecorder recorder = new MicrometerMetricsRecorder(registry);

        ObservabilityUpdateExceptionHandler handler =
                new ObservabilityUpdateExceptionHandler(recorder);

        Throwable exception = new IllegalStateException("boom");

        handler.handle(exception, null);

        Tags expectedTags = TelegramBotMetric.exception(exception);

        Counter c = registry.get(TelegramBotMetric.EXCEPTIONS_TOTAL.metricName())
                .tags(expectedTags)
                .counter();

        assertEquals(1.0, c.count(), 0.000001);
    }

    @Test
    void getOrder_shouldBeHighestPrecedence() {
        MetricsRecorder recorder = new MicrometerMetricsRecorder(new SimpleMeterRegistry());
        ObservabilityUpdateExceptionHandler handler =
                new ObservabilityUpdateExceptionHandler(recorder);

        assertEquals(Ordered.HIGHEST_PRECEDENCE, handler.getOrder());
    }

    @Test
    void terminal_shouldBeFalse() {
        MetricsRecorder recorder = new MicrometerMetricsRecorder(new SimpleMeterRegistry());
        ObservabilityUpdateExceptionHandler handler =
                new ObservabilityUpdateExceptionHandler(recorder);

        assertFalse(handler.terminal());
    }
}
