package io.ksilisk.telegrambot.observability.bpp;

import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.observability.marker.ObservabilityWrapped;
import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import io.ksilisk.telegrambot.observability.resolver.DefaultTelegramBotChannelResolver;
import io.ksilisk.telegrambot.observability.resolver.TelegramBotChannelResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * BeanPostProcessor that instruments {@link UpdateHandler} beans
 * to record handler-level metrics.
 */
public final class UpdateHandlerObservabilityBeanPostProcessor implements BeanPostProcessor, PriorityOrdered {
    private static final Logger log = LoggerFactory.getLogger(UpdateHandlerObservabilityBeanPostProcessor.class);

    private final MetricsRecorderResolver metricsRecorderResolver;
    private final ObjectProvider<TelegramBotChannelResolver> channelResolverObjectProvider;

    private TelegramBotChannelResolver cachedChannelResolver;

    public UpdateHandlerObservabilityBeanPostProcessor(ObjectProvider<MetricsRecorder> metricsRecorderProvider,
                                                       ObjectProvider<TelegramBotChannelResolver> channelResolverProvider) {
        this.metricsRecorderResolver = new MetricsRecorderResolver(metricsRecorderProvider, log);
        this.channelResolverObjectProvider = channelResolverProvider;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!(bean instanceof UpdateHandler)) {
            return bean;
        }
        if (bean instanceof ObservabilityWrapped) {
            return bean;
        }

        Class<?> userClass = ClassUtils.getUserClass(bean);
        String handlerId = userClass.getSimpleName();

        if (handlerId.isBlank()) {
            handlerId = userClass.getName();
        }

        boolean finalClass = Modifier.isFinal(userClass.getModifiers());
        boolean finalHandle = isFinalHandleMethod(userClass);

        if (finalClass || finalHandle) {
            log.warn("Observability: handler bean '{}' ({}) is final (classFinal={}, handleFinal={}); " +
                            "metrics instrumentation skipped.",
                    beanName, userClass.getName(), finalClass, finalHandle);
            return bean;
        }

        ProxyFactory pf = new ProxyFactory();
        pf.setTarget(bean);
        pf.setProxyTargetClass(true);
        pf.setInterfaces(ProxyInterfaces.mergeInterfacesDeep(userClass, ObservabilityWrapped.class));
        pf.addAdvice(new UpdateHandlerMetricsInterceptor(handlerId, bean,
                metricsRecorderResolver.get(), channelResolver()));
        return pf.getProxy();
    }

    private synchronized TelegramBotChannelResolver channelResolver() {
        if (cachedChannelResolver == null) {
            cachedChannelResolver = channelResolverObjectProvider.getIfAvailable(() -> {
                log.debug("No TelegramBotChannelResolver bean found in context; " +
                        "falling back to DefaultTelegramBotChannelResolver.");
                return new DefaultTelegramBotChannelResolver();
            });
        }
        return cachedChannelResolver;
    }

    private static boolean isFinalHandleMethod(Class<?> userClass) {
        for (Method m : userClass.getMethods()) {
            if (!"handle".equals(m.getName())) {
                continue;
            }
            if (m.getParameterCount() != 1) {
                continue;
            }
            if (Modifier.isFinal(m.getModifiers())) {
                return true;
            }
        }
        return false;
    }
}
