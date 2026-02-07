package io.ksilisk.telegrambot.observability.bpp;

import io.ksilisk.telegrambot.core.file.TelegramBotFileClient;
import io.ksilisk.telegrambot.observability.marker.ObservabilityWrapped;
import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
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
 * BeanPostProcessor that instruments {@link TelegramBotFileClient}
 * to record Telegram API file downloads.
 */
public final class TelegramBotFileClientObservabilityBeanPostProcessor implements BeanPostProcessor, PriorityOrdered {
    private static final Logger log = LoggerFactory.getLogger(TelegramBotFileClientObservabilityBeanPostProcessor.class);

    private final MetricsRecorderResolver metricsRecorderResolver;

    public TelegramBotFileClientObservabilityBeanPostProcessor(
            ObjectProvider<MetricsRecorder> metricsRecorderObjectProvider
    ) {
        this.metricsRecorderResolver = new MetricsRecorderResolver(metricsRecorderObjectProvider, log);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!(bean instanceof TelegramBotFileClient)) {
            return bean;
        }

        if (bean instanceof ObservabilityWrapped) {
            return bean;
        }

        Class<?> userClass = ClassUtils.getUserClass(bean);

        boolean finalClass = Modifier.isFinal(userClass.getModifiers());
        boolean finalMethods = hasFinalFileClientMethod(userClass);

        if (finalClass || finalMethods) {
            log.warn(
                    "Observability: TelegramBotFileClient bean '{}' ({}) is final (classFinal={}, methodsFinal={}); " +
                            "file metrics instrumentation skipped.",
                    beanName, userClass.getName(), finalClass, finalMethods
            );
            return bean;
        }

        ProxyFactory pf = new ProxyFactory();
        pf.setTarget(bean);
        pf.setProxyTargetClass(true);
        pf.setInterfaces(ProxyInterfaces.mergeInterfacesDeep(userClass, ObservabilityWrapped.class));
        pf.addAdvice(new TelegramBotFileClientMetricsInterceptor(metricsRecorderResolver.get()));
        return pf.getProxy();
    }

    private static boolean hasFinalFileClientMethod(Class<?> userClass) {
        for (Method m : userClass.getMethods()) {
            String name = m.getName();
            if (!isFileClientMethodName(name)) {
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

    private static boolean isFileClientMethodName(String name) {
        return "downloadByPath".equals(name)
                || "openStreamByPath".equals(name)
                || "downloadById".equals(name)
                || "openStreamById".equals(name);
    }
}
