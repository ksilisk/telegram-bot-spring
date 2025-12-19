package io.ksilisk.telegrambot.observability.bpp;

import io.ksilisk.telegrambot.core.delivery.DeliveryThreadPoolExecutorFactory;
import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import io.micrometer.core.instrument.Tags;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Registers delivery thread pool gauges for a {@link ThreadPoolExecutor}
 * created by {@link DeliveryThreadPoolExecutorFactory}.
 */
final class DeliveryThreadPoolExecutorMetricsInterceptor implements MethodInterceptor {
    private static final Tags NO_TAGS = Tags.empty();

    private final MetricsRecorder metrics;
    private volatile boolean gaugesRegistered;

    DeliveryThreadPoolExecutorMetricsInterceptor(MetricsRecorder metricsRecorder) {
        this.metrics = metricsRecorder;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        if (!isBuildMethod(method, invocation.getArguments())) {
            return invocation.proceed();
        }

        Object result = invocation.proceed();
        if (!(result instanceof ThreadPoolExecutor executor)) {
            return result;
        }

        registerGaugesOnce(executor);
        return executor;
    }

    private static boolean isBuildMethod(Method method, Object[] args) {
        return method != null
                && method.getName().equals("buildThreadPoolExecutor")
                && (args == null || args.length == 0);
    }

    private synchronized void registerGaugesOnce(ThreadPoolExecutor executor) {
        if (gaugesRegistered) {
            return;
        }
        metrics.registerGauge(TelegramBotMetric.DELIVERY_POOL_ACTIVE, NO_TAGS, executor::getActiveCount);
        metrics.registerGauge(TelegramBotMetric.DELIVERY_POOL_SIZE, NO_TAGS, executor::getPoolSize);
        metrics.registerGauge(TelegramBotMetric.DELIVERY_POOL_QUEUE_SIZE, NO_TAGS, () -> executor.getQueue().size());
        gaugesRegistered = true;
    }
}
