package io.ksilisk.telegrambot.observability.metrics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TelegramBotMetricTest {
    @Test
    void allMetricsShouldHaveNameAndDescription() {
        for (TelegramBotMetric metric : TelegramBotMetric.values()) {
            assertNotNull(metric.metricName(),
                    () -> "metricName is null for " + metric.name());
            assertFalse(metric.metricName().isBlank(),
                    () -> "metricName is blank for " + metric.name());

            assertNotNull(metric.description(),
                    () -> "description is null for " + metric.name());
            assertFalse(metric.description().isBlank(),
                    () -> "description is blank for " + metric.name());
        }
    }
}
