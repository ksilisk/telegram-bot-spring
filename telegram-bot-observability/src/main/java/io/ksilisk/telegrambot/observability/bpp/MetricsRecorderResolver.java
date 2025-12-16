package io.ksilisk.telegrambot.observability.bpp;

import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import io.ksilisk.telegrambot.observability.recorder.impl.NoopMetricsRecorder;
import org.slf4j.Logger;
import org.springframework.beans.factory.ObjectProvider;

final class MetricsRecorderResolver {
    private final ObjectProvider<MetricsRecorder> metricsRecorderObjectProvider;
    private final Logger log;

    private MetricsRecorder cachedMetricsRecorder;

    public MetricsRecorderResolver(ObjectProvider<MetricsRecorder> metricsRecorderObjectProvider, Logger log) {
        this.metricsRecorderObjectProvider = metricsRecorderObjectProvider;
        this.log = log;
    }

    public synchronized MetricsRecorder get() {
        if (cachedMetricsRecorder == null) {
            cachedMetricsRecorder = metricsRecorderObjectProvider.getIfAvailable(() -> {
                log.debug("No MetricsRecorder bean found in context; falling back to NoopMetricsRecorder.");
                return new NoopMetricsRecorder();
            });
        }
        return cachedMetricsRecorder;
    }
}
