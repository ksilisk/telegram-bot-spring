package io.ksilisk.telegrambot.observability.bpp;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.delivery.UpdateDelivery;
import io.ksilisk.telegrambot.observability.TelegramBotObservabilityAutoConfiguration;
import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateDeliveryObservabilityBeanPostProcessorTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    TelegramBotObservabilityAutoConfiguration.class
            ))
            .withBean(SimpleMeterRegistry.class, SimpleMeterRegistry::new)
            .withBean(UpdateDelivery.class, () -> Mockito.mock(UpdateDelivery.class));

    @Test
    void shouldRecordUpdatesReceivedAndBatchSize() {
        contextRunner.run(ctx -> {
            UpdateDelivery delivery = ctx.getBean(UpdateDelivery.class);
            SimpleMeterRegistry registry = ctx.getBean(SimpleMeterRegistry.class);

            delivery.deliver(List.of(new Update(), new Update(), new Update()));

            double received = registry
                    .get(TelegramBotMetric.UPDATES_RECEIVED.metricName())
                    .tags(Tags.empty())
                    .counter()
                    .count();

            assertEquals(3.0, received, 0.000001);

            DistributionSummary batchSize = registry
                    .get(TelegramBotMetric.UPDATES_BATCH_SIZE.metricName())
                    .tags(Tags.empty())
                    .summary();

            assertEquals(1L, batchSize.count());
            assertEquals(3.0, batchSize.totalAmount(), 0.000001);
        });
    }
}
