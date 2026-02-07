package io.ksilisk.telegrambot.observability.bpp;

import io.ksilisk.telegrambot.core.exception.file.TelegramFileDownloadException;
import io.ksilisk.telegrambot.core.file.TelegramBotFileClient;
import io.ksilisk.telegrambot.observability.TelegramBotObservabilityAutoConfiguration;
import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TelegramBotFileClientObservabilityBeanPostProcessorTest {
    private final ApplicationContextRunner baseRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(TelegramBotObservabilityAutoConfiguration.class))
            .withBean(SimpleMeterRegistry.class, SimpleMeterRegistry::new);

    @Test
    void shouldRecordFileCallsAndDuration_andBytes_onSuccess_downloadByPath() {
        baseRunner
                .withBean(SuccessFileClient.class, SuccessFileClient::new)
                .run(ctx -> {
                    TelegramBotFileClient fileClient = ctx.getBean(TelegramBotFileClient.class);
                    SimpleMeterRegistry registry = ctx.getBean(SimpleMeterRegistry.class);

                    byte[] bytes = fileClient.downloadByPath("photos/file_1.png");
                    assertNotNull(bytes);
                    assertEquals(3, bytes.length);

                    Tags tags = expectedTags("downloadByPath", TelegramBotMetric.Status.SUCCESS.tagValue());

                    Counter calls = registry.get(TelegramBotMetric.FILE_CALLS.metricName())
                            .tags(tags)
                            .counter();
                    assertEquals(1.0, calls.count(), 0.000001);

                    Timer duration = registry.get(TelegramBotMetric.FILE_DOWNLOAD_DURATION.metricName())
                            .tags(tags)
                            .timer();
                    assertEquals(1L, duration.count());
                    assertTrue(duration.totalTime(TimeUnit.NANOSECONDS) >= 0.0);

                    var summary = registry.get(TelegramBotMetric.FILE_BYTES.metricName())
                            .tags(Tags.of(TelegramBotMetric.TAG_METHOD, "downloadByPath"))
                            .summary();
                    assertEquals(1L, summary.count());
                    assertEquals(3.0, summary.totalAmount(), 0.000001);
                });
    }

    @Test
    void shouldRecordFileCallsAndDuration_onError_downloadByPath() {
        baseRunner
                .withBean(ErrorFileClient.class, ErrorFileClient::new)
                .run(ctx -> {
                    TelegramBotFileClient fileClient = ctx.getBean(TelegramBotFileClient.class);
                    SimpleMeterRegistry registry = ctx.getBean(SimpleMeterRegistry.class);

                    try {
                        fileClient.downloadByPath("photos/missing.png");
                        fail("Expected IOException");
                    } catch (Exception ignored) {
                        // expected
                    }

                    Tags tags = expectedTags("downloadByPath", TelegramBotMetric.Status.ERROR.tagValue());

                    Counter calls = registry.get(TelegramBotMetric.FILE_CALLS.metricName())
                            .tags(tags)
                            .counter();
                    assertEquals(1.0, calls.count(), 0.000001);

                    Timer duration = registry.get(TelegramBotMetric.FILE_DOWNLOAD_DURATION.metricName())
                            .tags(tags)
                            .timer();
                    assertEquals(1L, duration.count());

                    assertThrows(Exception.class, () ->
                            registry.get(TelegramBotMetric.FILE_BYTES.metricName())
                                    .tags(Tags.of(TelegramBotMetric.TAG_METHOD, "downloadByPath"))
                                    .summary()
                    );
                });
    }

    @Test
    void shouldRecordFileCallsAndDuration_onSuccess_openStreamByPath() {
        baseRunner
                .withBean(SuccessFileClient.class, SuccessFileClient::new)
                .run(ctx -> {
                    TelegramBotFileClient fileClient = ctx.getBean(TelegramBotFileClient.class);
                    SimpleMeterRegistry registry = ctx.getBean(SimpleMeterRegistry.class);

                    try (InputStream ignored = fileClient.openStreamByPath("photos/file_1.png")) {
                        // do nothing
                    } catch (IOException e) {
                        fail(e);
                    }

                    Tags tags = expectedTags("openStreamByPath", TelegramBotMetric.Status.SUCCESS.tagValue());

                    Counter calls = registry.get(TelegramBotMetric.FILE_CALLS.metricName())
                            .tags(tags)
                            .counter();
                    assertEquals(1.0, calls.count(), 0.000001);

                    Timer duration = registry.get(TelegramBotMetric.FILE_STREAM_OPEN_DURATION.metricName())
                            .tags(tags)
                            .timer();
                    assertEquals(1L, duration.count());
                });
    }

    private static Tags expectedTags(String method, String status) {
        return Tags.of(
                TelegramBotMetric.TAG_METHOD, method,
                TelegramBotMetric.TAG_STATUS, status
        );
    }

    static class SuccessFileClient implements TelegramBotFileClient {

        @Override
        public byte[] downloadByPath(String filePath) {
            return new byte[]{1, 2, 3};
        }

        @Override
        public InputStream openStreamByPath(String filePath) {
            return new ByteArrayInputStream(new byte[]{1, 2, 3});
        }

        @Override
        public byte[] downloadById(String fileId) {
            return new byte[]{4, 5};
        }

        @Override
        public InputStream openStreamById(String fileId) {
            return new ByteArrayInputStream(new byte[]{4, 5});
        }
    }

    static class ErrorFileClient implements TelegramBotFileClient {

        @Override
        public byte[] downloadByPath(String filePath) {
            throw new TelegramFileDownloadException("boom");
        }

        @Override
        public InputStream openStreamByPath(String filePath) {
            throw new TelegramFileDownloadException("boom");
        }

        @Override
        public byte[] downloadById(String fileId) {
            throw new TelegramFileDownloadException("boom");
        }

        @Override
        public InputStream openStreamById(String fileId) {
            throw new TelegramFileDownloadException("boom");
        }
    }
}
