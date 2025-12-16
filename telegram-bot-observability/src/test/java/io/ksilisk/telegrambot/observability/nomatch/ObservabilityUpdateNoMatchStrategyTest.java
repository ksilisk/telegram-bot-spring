package io.ksilisk.telegrambot.observability.nomatch;

import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import io.ksilisk.telegrambot.observability.recorder.impl.MicrometerMetricsRecorder;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.core.Ordered;

import static org.junit.jupiter.api.Assertions.*;

class ObservabilityUpdateNoMatchStrategyTest {
    @Test
    void handle_shouldIncrementRoutingNoMatchCounter() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        MetricsRecorder recorder = new MicrometerMetricsRecorder(registry);

        ObservabilityUpdateNoMatchStrategy strategy = new ObservabilityUpdateNoMatchStrategy(recorder);

        strategy.handle(null);

        Counter c = registry.get(TelegramBotMetric.ROUTING_NO_MATCH.metricName())
                .tags(Tags.empty())
                .counter();

        assertEquals(1.0, c.count(), 0.000001);
    }

    @Test
    void getOrder_shouldBeHighestPrecedence() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        MetricsRecorder recorder = new MicrometerMetricsRecorder(registry);

        ObservabilityUpdateNoMatchStrategy strategy = new ObservabilityUpdateNoMatchStrategy(recorder);

        assertEquals(Ordered.HIGHEST_PRECEDENCE, strategy.getOrder());
    }
}
