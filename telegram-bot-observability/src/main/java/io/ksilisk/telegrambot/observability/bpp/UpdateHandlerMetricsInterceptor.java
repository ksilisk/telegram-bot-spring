package io.ksilisk.telegrambot.observability.bpp;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.observability.metrics.TelegramBotChannel;
import io.ksilisk.telegrambot.observability.metrics.TelegramBotMetric;
import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import io.ksilisk.telegrambot.observability.resolver.TelegramBotChannelResolver;
import io.micrometer.core.instrument.Tags;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * Records handler invocation and execution time metrics for {@link UpdateHandler#handle}.
 */
final class UpdateHandlerMetricsInterceptor implements MethodInterceptor {

    private final String handlerId;
    private final Object target;
    private final MetricsRecorder metrics;
    private final TelegramBotChannelResolver channelResolver;

    UpdateHandlerMetricsInterceptor(String beanName, Object target, MetricsRecorder metrics,
                                    TelegramBotChannelResolver channelResolver) {
        this.handlerId = beanName;
        this.target = target;
        this.metrics = metrics;
        this.channelResolver = channelResolver;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        if (!isHandleMethod(method, invocation.getArguments())) {
            return invocation.proceed();
        }

        Update update = (Update) invocation.getArguments()[0];

        TelegramBotChannel channel = channelResolver.resolve(target);
        Tags base = TelegramBotMetric.handlerChannel(handlerId, channel);

        long start = System.nanoTime();
        TelegramBotMetric.Status status = TelegramBotMetric.Status.SUCCESS;

        try {
            return invocation.proceed();
        } catch (Throwable t) {
            status = TelegramBotMetric.Status.ERROR;
            throw t;
        } finally {
            metrics.increment(
                    TelegramBotMetric.HANDLER_INVOCATIONS,
                    base.and(TelegramBotMetric.status(status))
                            .and(TelegramBotMetric.updateType(update))
            );

            metrics.recordTimer(
                    TelegramBotMetric.HANDLER_DURATION,
                    base.and(TelegramBotMetric.status(status))
                            .and(TelegramBotMetric.updateType(update)),
                    System.nanoTime() - start
            );
        }
    }

    private static boolean isHandleMethod(Method method, Object[] args) {
        return "handle".equals(method.getName()) && args != null && args.length == 1;
    }
}
