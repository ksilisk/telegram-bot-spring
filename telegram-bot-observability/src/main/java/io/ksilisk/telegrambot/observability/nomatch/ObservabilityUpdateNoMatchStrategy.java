package io.ksilisk.telegrambot.observability.nomatch;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.strategy.UpdateNoMatchStrategy;
import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import org.springframework.core.Ordered;

public class ObservabilityUpdateNoMatchStrategy implements UpdateNoMatchStrategy {
    private final MetricsRecorder metricsRecorder;

    public ObservabilityUpdateNoMatchStrategy(MetricsRecorder metricsRecorder) {
        this.metricsRecorder = metricsRecorder;
    }

    @Override
    public void handle(Update update) {
        metricsRecorder.increment(TelegramBotMetric.ROUTING_NO_MATCH, TelegramBotMetric.updateType(update));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
