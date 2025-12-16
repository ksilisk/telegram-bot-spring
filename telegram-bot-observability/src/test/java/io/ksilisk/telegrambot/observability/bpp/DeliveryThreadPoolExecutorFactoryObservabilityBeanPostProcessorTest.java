package io.ksilisk.telegrambot.observability.bpp;

import io.ksilisk.telegrambot.core.delivery.DeliveryThreadPoolExecutorFactory;
import io.ksilisk.telegrambot.observability.TelegramBotObservabilityAutoConfiguration;
import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryThreadPoolExecutorFactoryObservabilityBeanPostProcessorTest {
    private final ApplicationContextRunner baseRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(TelegramBotObservabilityAutoConfiguration.class))
            .withBean(SimpleMeterRegistry.class, SimpleMeterRegistry::new);

    @Test
    void shouldRegisterDeliveryPoolGaugesAndReflectValues() {
        baseRunner
                .withBean(DeliveryThreadPoolExecutorFactory.class, TestFactory::new)
                .run(ctx -> {
                    DeliveryThreadPoolExecutorFactory factory = ctx.getBean(DeliveryThreadPoolExecutorFactory.class);
                    SimpleMeterRegistry registry = ctx.getBean(SimpleMeterRegistry.class);

                    ThreadPoolExecutor executor = factory.buildThreadPoolExecutor();

                    // Gauges should be registered
                    Gauge active = registry.find(TelegramBotMetric.DELIVERY_POOL_ACTIVE.metricName())
                            .tags(Tags.empty())
                            .gauge();
                    Gauge poolSize = registry.find(TelegramBotMetric.DELIVERY_POOL_SIZE.metricName())
                            .tags(Tags.empty())
                            .gauge();
                    Gauge queueSize = registry.find(TelegramBotMetric.DELIVERY_POOL_QUEUE_SIZE.metricName())
                            .tags(Tags.empty())
                            .gauge();

                    assertNotNull(active, "DELIVERY_POOL_ACTIVE gauge must be registered");
                    assertNotNull(poolSize, "DELIVERY_POOL_SIZE gauge must be registered");
                    assertNotNull(queueSize, "DELIVERY_POOL_QUEUE_SIZE gauge must be registered");

                    // Initially: pool may be 0 until first task; queue should be 0
                    assertEquals(0.0, queueSize.value(), 0.000001);

                    // Now make queue size > 0 deterministically:
                    // core=1,max=1 queue=1 => first task occupies thread, second goes to queue
                    CountDownLatch started = new CountDownLatch(1);
                    CountDownLatch release = new CountDownLatch(1);

                    executor.execute(() -> {
                        started.countDown();
                        try {
                            release.await(3, TimeUnit.SECONDS);
                        } catch (InterruptedException ignored) {
                            Thread.currentThread().interrupt();
                        }
                    });

                    assertTrue(started.await(1, TimeUnit.SECONDS), "First task should start");

                    executor.execute(() -> { /* should get queued */ });

                    // queue size should become 1 (or at least >0)
                    assertTrue(queueSize.value() >= 1.0, "Queue size gauge should reflect queued task");

                    // active should be >= 1 while first task is running
                    assertTrue(active.value() >= 1.0, "Active gauge should reflect running task");

                    release.countDown();
                    executor.shutdownNow();
                });
    }

    @Test
    void finalClassFactory_shouldNotBeInstrumented() {
        baseRunner
                .withBean(DeliveryThreadPoolExecutorFactory.class, FinalClassFactory::new)
                .run(ctx -> {
                    DeliveryThreadPoolExecutorFactory factory = ctx.getBean(DeliveryThreadPoolExecutorFactory.class);
                    SimpleMeterRegistry registry = ctx.getBean(SimpleMeterRegistry.class);

                    ThreadPoolExecutor executor = factory.buildThreadPoolExecutor();
                    executor.shutdownNow();

                    assertNull(registry.find(TelegramBotMetric.DELIVERY_POOL_ACTIVE.metricName()).tags(Tags.empty()).gauge());
                    assertNull(registry.find(TelegramBotMetric.DELIVERY_POOL_SIZE.metricName()).tags(Tags.empty()).gauge());
                    assertNull(registry.find(TelegramBotMetric.DELIVERY_POOL_QUEUE_SIZE.metricName()).tags(Tags.empty()).gauge());
                });
    }

    @Test
    void finalBuildMethodFactory_shouldNotBeInstrumented() {
        baseRunner
                .withBean(DeliveryThreadPoolExecutorFactory.class, FinalMethodFactory::new)
                .run(ctx -> {
                    DeliveryThreadPoolExecutorFactory factory = ctx.getBean(DeliveryThreadPoolExecutorFactory.class);
                    SimpleMeterRegistry registry = ctx.getBean(SimpleMeterRegistry.class);

                    ThreadPoolExecutor executor = factory.buildThreadPoolExecutor();
                    executor.shutdownNow();

                    assertNull(registry.find(TelegramBotMetric.DELIVERY_POOL_ACTIVE.metricName()).tags(Tags.empty()).gauge());
                    assertNull(registry.find(TelegramBotMetric.DELIVERY_POOL_SIZE.metricName()).tags(Tags.empty()).gauge());
                    assertNull(registry.find(TelegramBotMetric.DELIVERY_POOL_QUEUE_SIZE.metricName()).tags(Tags.empty()).gauge());
                });
    }


    static class TestFactory implements DeliveryThreadPoolExecutorFactory {
        @Override
        public ThreadPoolExecutor buildThreadPoolExecutor() {
            // core=1,max=1 to make queueing deterministic in the test
            return new ThreadPoolExecutor(
                    1,
                    1,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(1)
            );
        }
    }

    public static final class FinalClassFactory implements DeliveryThreadPoolExecutorFactory {
        @Override
        public ThreadPoolExecutor buildThreadPoolExecutor() {
            return new ThreadPoolExecutor(
                    1,
                    1,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(1)
            );
        }
    }

    public static class FinalMethodFactory implements DeliveryThreadPoolExecutorFactory {
        @Override
        public final ThreadPoolExecutor buildThreadPoolExecutor() {
            return new ThreadPoolExecutor(
                    1,
                    1,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(1)
            );
        }
    }
}
