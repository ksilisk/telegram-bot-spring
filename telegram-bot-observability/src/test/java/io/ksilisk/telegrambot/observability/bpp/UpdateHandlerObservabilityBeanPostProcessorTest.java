package io.ksilisk.telegrambot.observability.bpp;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import io.ksilisk.telegrambot.observability.TelegramBotObservabilityAutoConfiguration;
import io.ksilisk.telegrambot.observability.metrics.TelegramBotChannel;
import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric.*;
import static org.junit.jupiter.api.Assertions.*;

class UpdateHandlerObservabilityBeanPostProcessorTest {
    private final ApplicationContextRunner baseRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(TelegramBotObservabilityAutoConfiguration.class))
            .withBean(SimpleMeterRegistry.class, SimpleMeterRegistry::new);

    @Test
    void shouldRecordHandlerInvocationAndDuration_onSuccess() {
        baseRunner
                .withBean(CommandUpdateHandler.class, SuccessCommandHandler::new)
                .run(ctx -> {
                    SuccessCommandHandler handler = ctx.getBean(SuccessCommandHandler.class);
                    SimpleMeterRegistry registry = ctx.getBean(SimpleMeterRegistry.class);

                    handler.handle(new Update());

                    Tags tags = expectedTags(TelegramBotChannel.COMMAND.tagValue(),
                            SuccessCommandHandler.class.getSimpleName(), Status.SUCCESS.tagValue());

                    Counter invocations = registry.get(TelegramBotMetric.HANDLER_INVOCATIONS.metricName())
                            .tags(tags)
                            .counter();
                    assertEquals(1.0, invocations.count(), 0.000001);

                    Timer duration = registry.get(TelegramBotMetric.HANDLER_DURATION.metricName())
                            .tags(tags)
                            .timer();
                    assertEquals(1L, duration.count());
                    assertTrue(duration.totalTime(TimeUnit.NANOSECONDS) >= 0.0);
                });
    }

    @Test
    void shouldRecordHandlerInvocationAndDuration_onError() {
        baseRunner
                .withBean(ErrorCommandHandler.class, ErrorCommandHandler::new)
                .run(ctx -> {
                    ErrorCommandHandler handler = ctx.getBean(ErrorCommandHandler.class);
                    SimpleMeterRegistry registry = ctx.getBean(SimpleMeterRegistry.class);

                    try {
                        handler.handle(new Update());
                    } catch (RuntimeException ignored) {
                        // expected
                    }

                    Tags tags = expectedTags(TelegramBotChannel.COMMAND.tagValue(),
                            ErrorCommandHandler.class.getSimpleName(), Status.ERROR.tagValue());

                    Counter invocations = registry.get(TelegramBotMetric.HANDLER_INVOCATIONS.metricName())
                            .tags(tags)
                            .counter();
                    assertEquals(1.0, invocations.count(), 0.000001);

                    Timer duration = registry.get(TelegramBotMetric.HANDLER_DURATION.metricName())
                            .tags(tags)
                            .timer();
                    assertEquals(1L, duration.count());
                });
    }

    @Test
    void finalClassHandler_shouldNotBeInstrumented() {
        baseRunner
                .withBean(FinalCommandHandler.class, FinalCommandHandler::new)
                .run(ctx -> {
                    FinalCommandHandler handler = ctx.getBean(FinalCommandHandler.class);
                    SimpleMeterRegistry registry = ctx.getBean(SimpleMeterRegistry.class);

                    handler.handle(null);

                    Tags tags = expectedTags(TelegramBotChannel.COMMAND.tagValue(),
                            FinalCommandHandler.class.getSimpleName(), Status.SUCCESS.tagValue());

                    assertNull(registry.find(TelegramBotMetric.HANDLER_INVOCATIONS.metricName()).tags(tags).counter());
                    assertNull(registry.find(TelegramBotMetric.HANDLER_DURATION.metricName()).tags(tags).timer());
                });

    }

    @Test
    void finalHandleMethod_shouldNotBeInstrumented() {
        baseRunner
                .withBean(FinalHandleMethodHandler.class, FinalHandleMethodHandler::new)
                .run(ctx -> {
                    FinalHandleMethodHandler handler = ctx.getBean(FinalHandleMethodHandler.class);
                    SimpleMeterRegistry registry = ctx.getBean(SimpleMeterRegistry.class);

                    handler.handle(null);

                    Tags tags = expectedTags(TelegramBotChannel.COMMAND.tagValue(),
                            FinalHandleMethodHandler.class.getSimpleName(), Status.SUCCESS.tagValue());

                    assertNull(registry.find(TelegramBotMetric.HANDLER_INVOCATIONS.metricName()).tags(tags).counter());
                    assertNull(registry.find(TelegramBotMetric.HANDLER_DURATION.metricName()).tags(tags).timer());
                });
    }


    private static Tags expectedTags(String channel, String handler, String status) {
        return Tags.of(
                TAG_CHANNEL, channel,
                TAG_HANDLER, handler,
                TAG_STATUS, status
        );
    }

    static class SuccessCommandHandler implements CommandUpdateHandler {
        @Override
        public void handle(Update update) {
            // no-op
        }

        @Override
        public Set<String> commands() {
            return Set.of("/test1");
        }
    }

    static class ErrorCommandHandler implements CommandUpdateHandler {
        @Override
        public void handle(Update update) {
            throw new IllegalStateException("boom");
        }

        @Override
        public Set<String> commands() {
            return Set.of("/test");
        }
    }

    static final class FinalCommandHandler implements CommandUpdateHandler {
        @Override
        public void handle(Update update) {
            // no-op
        }

        @Override
        public Set<String> commands() {
            return Set.of("/final1");
        }
    }

    static class FinalHandleMethodHandler implements CommandUpdateHandler {
        @Override
        public final void handle(Update update) {
            // no-op
        }

        @Override
        public Set<String> commands() {
            return Set.of("/final");
        }
    }
}
