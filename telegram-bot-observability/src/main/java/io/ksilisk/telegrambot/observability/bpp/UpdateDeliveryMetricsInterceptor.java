package io.ksilisk.telegrambot.observability.bpp;

import io.ksilisk.telegrambot.core.delivery.UpdateDelivery;
import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import io.micrometer.core.instrument.Tags;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Records delivery-level metrics for {@link UpdateDelivery#deliver(List)}.
 */
final class UpdateDeliveryMetricsInterceptor implements MethodInterceptor {
    private final MetricsRecorder metrics;

    UpdateDeliveryMetricsInterceptor(MetricsRecorder metrics) {
        this.metrics = metrics;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method m = invocation.getMethod();
        Object[] args = invocation.getArguments();

        if (isDeliver(m, args)) {
            @SuppressWarnings("unchecked")
            List<Object> updates = (List<Object>) args[0];
            long n = (updates == null) ? 0L : updates.size();

            metrics.increment(TelegramBotMetric.UPDATES_RECEIVED, Tags.empty(), n);
            metrics.recordSummary(TelegramBotMetric.UPDATES_BATCH_SIZE, Tags.empty(), n);

            return invocation.proceed();
        }

        return invocation.proceed();
    }

    private static boolean isDeliver(Method m, Object[] args) {
        return "deliver".equals(m.getName())
                && args != null
                && args.length == 1
                && args[0] instanceof List<?>;
    }
}
