package io.ksilisk.telegrambot.observability.bpp;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import io.ksilisk.telegrambot.core.exception.request.TelegramRequestException;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.observability.TelegramBotObservabilityAutoConfiguration;
import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TelegramBotExecutorObservabilityBeanPostProcessorTest {
    private final ApplicationContextRunner baseRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(TelegramBotObservabilityAutoConfiguration.class))
            .withBean(SimpleMeterRegistry.class, SimpleMeterRegistry::new);

    @Test
    void shouldRecordApiCallsAndDuration_onSuccess() {
        baseRunner
                .withBean(SuccessExecutor.class, SuccessExecutor::new)
                .run(ctx -> {
                    TelegramBotExecutor executor = ctx.getBean(TelegramBotExecutor.class);
                    SimpleMeterRegistry registry = ctx.getBean(SimpleMeterRegistry.class);

                    SendMessage request = new SendMessage(1, "test");
                    executor.execute(request);

                    Tags tags = expectedTags("SendMessage", TelegramBotMetric.Status.SUCCESS.tagValue());

                    Counter calls = registry.get(TelegramBotMetric.API_CALLS.metricName())
                            .tags(tags)
                            .counter();
                    assertEquals(1.0, calls.count(), 0.000001);

                    Timer duration = registry.get(TelegramBotMetric.API_DURATION.metricName())
                            .tags(tags)
                            .timer();
                    assertEquals(1L, duration.count());
                    assertTrue(duration.totalTime(TimeUnit.NANOSECONDS) >= 0.0);
                });
    }

    @Test
    void shouldRecordApiCallsAndDuration_onError() {
        baseRunner
                .withBean(ErrorExecutor.class, ErrorExecutor::new)
                .run(ctx -> {
                    TelegramBotExecutor executor = ctx.getBean(TelegramBotExecutor.class);
                    SimpleMeterRegistry registry = ctx.getBean(SimpleMeterRegistry.class);

                    SendMessage request = new SendMessage(1, "");
                    try {
                        executor.execute(request);
                    } catch (RuntimeException ignored) {
                        // expected
                    }

                    Tags tags = expectedTags("SendMessage", TelegramBotMetric.Status.ERROR.tagValue());

                    Counter calls = registry.get(TelegramBotMetric.API_CALLS.metricName())
                            .tags(tags)
                            .counter();
                    assertEquals(1.0, calls.count(), 0.000001);

                    Timer duration = registry.get(TelegramBotMetric.API_DURATION.metricName())
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

    static class SuccessExecutor implements TelegramBotExecutor {

        @Override
        public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(
                BaseRequest<T, R> request) throws TelegramRequestException {
            return null;
        }
    }

    static class ErrorExecutor implements TelegramBotExecutor {

        @Override
        public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(
                BaseRequest<T, R> request) throws TelegramRequestException {
            throw new TelegramRequestException("boom");
        }
    }
}
