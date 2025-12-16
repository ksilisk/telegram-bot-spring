package io.ksilisk.telegrambot.observability.bpp;

import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import io.ksilisk.telegrambot.observability.recorder.impl.NoopMetricsRecorder;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MetricsRecorderResolverTest {
    @Test
    void shouldResolveRecorder() {
        MetricsRecorder recorder = new NoopMetricsRecorder();

        ObjectProvider<MetricsRecorder> provider = mock(ObjectProvider.class);
        when(provider.getIfAvailable(any()))
                .thenReturn(recorder);

        MetricsRecorderResolver resolver =
                new MetricsRecorderResolver(provider, LoggerFactory.getLogger("test"));

        MetricsRecorder resolved = resolver.get();

        assertSame(recorder, resolved);
    }
}
