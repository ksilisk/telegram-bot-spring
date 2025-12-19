package io.ksilisk.telegrambot.observability.handler.exception;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.exception.UpdateExceptionHandler;
import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import org.springframework.core.Ordered;

public class ObservabilityUpdateExceptionHandler implements UpdateExceptionHandler {
    private final MetricsRecorder metricsRecorder;

    public ObservabilityUpdateExceptionHandler(MetricsRecorder metricsRecorder) {
        this.metricsRecorder = metricsRecorder;
    }

    @Override
    public boolean supports(Throwable t, Update update) {
        return true;
    }

    @Override
    public void handle(Throwable t, Update update) {
        metricsRecorder.increment(TelegramBotMetric.EXCEPTIONS_TOTAL, TelegramBotMetric.exception(t));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
