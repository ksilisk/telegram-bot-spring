package io.ksilisk.telegrambot.observability.bpp;

import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import io.micrometer.core.instrument.Tags;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * Records Telegram API file downloads metrics for {@link io.ksilisk.telegrambot.core.file.TelegramBotFileClient}.
 */
final class TelegramBotFileClientMetricsInterceptor implements MethodInterceptor {
    private final MetricsRecorder metrics;

    TelegramBotFileClientMetricsInterceptor(MetricsRecorder metrics) {
        this.metrics = metrics;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Object[] args = invocation.getArguments();

        if (!isFileClientMethod(method, args)) {
            return invocation.proceed();
        }

        String op = method.getName(); // downloadByPath/openStreamByPath/downloadById/openStreamById
        Tags base = TelegramBotMetric.method(op);

        long start = System.nanoTime();
        TelegramBotMetric.Status status = TelegramBotMetric.Status.SUCCESS;

        Object result = null;
        try {
            result = invocation.proceed();
            return result;
        } catch (Throwable t) {
            status = TelegramBotMetric.Status.ERROR;
            throw t;
        } finally {
            Tags tags = base.and(TelegramBotMetric.status(status));

            TelegramBotMetric durationMetric = isDownloadOperation(op) ?
                    TelegramBotMetric.FILE_DOWNLOAD_DURATION : TelegramBotMetric.FILE_STREAM_OPEN_DURATION;

            metrics.increment(TelegramBotMetric.FILE_CALLS, tags);
            metrics.recordTimer(durationMetric, tags, System.nanoTime() - start);


            if (status == TelegramBotMetric.Status.SUCCESS && isDownloadOperation(op) && result instanceof byte[] bytes) {
                metrics.recordSummary(TelegramBotMetric.FILE_BYTES, base, bytes.length);
            }
        }
    }

    private static boolean isFileClientMethod(Method method, Object[] args) {
        if (args == null || args.length != 1 || args[0] == null) {
            return false;
        }

        String name = method.getName();
        return "downloadByPath".equals(name)
                || "openStreamByPath".equals(name)
                || "downloadById".equals(name)
                || "openStreamById".equals(name);
    }

    private static boolean isDownloadOperation(String op) {
        return "downloadByPath".equals(op) || "downloadById".equals(op);
    }
}
