package io.ksilisk.telegrambot.observability.bpp;

import com.pengrad.telegrambot.request.BaseRequest;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import io.micrometer.core.instrument.Tags;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;

/**
 * Records Telegram API call metrics for {@link TelegramBotExecutor#execute(BaseRequest)}}.
 */
final class TelegramBotExecutorMetricsInterceptor implements MethodInterceptor {
    private final MetricsRecorder metrics;

    TelegramBotExecutorMetricsInterceptor(MetricsRecorder metrics) {
        this.metrics = metrics;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Object[] args = invocation.getArguments();

        if (!isExecuteMethod(method, args)) {
            return invocation.proceed();
        }

        Object request = args[0];
        String apiMethod = resolveApiMethodName(request);

        Tags base = TelegramBotMetric.method(apiMethod);

        long start = System.nanoTime();
        TelegramBotMetric.Status status = TelegramBotMetric.Status.SUCCESS;

        try {
            return invocation.proceed();
        } catch (Throwable t) {
            status = TelegramBotMetric.Status.ERROR;
            throw t;
        } finally {
            metrics.increment(
                    TelegramBotMetric.API_CALLS,
                    base.and(TelegramBotMetric.status(status))
            );

            metrics.recordTimer(
                    TelegramBotMetric.API_DURATION,
                    base.and(TelegramBotMetric.status(status)),
                    System.nanoTime() - start
            );
        }
    }

    private static boolean isExecuteMethod(Method method, Object[] args) {
        return "execute".equals(method.getName())
                && args != null
                && args.length == 1
                && args[0] != null;
    }

    private static String resolveApiMethodName(Object request) {
        Class<?> c = ClassUtils.getUserClass(request);
        String simple = c.getSimpleName();
        return simple.isBlank() ? c.getName() : simple;
    }
}
