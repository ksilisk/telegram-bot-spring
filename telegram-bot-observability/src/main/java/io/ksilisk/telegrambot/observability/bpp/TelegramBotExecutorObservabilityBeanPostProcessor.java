package io.ksilisk.telegrambot.observability.bpp;

import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
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
 * BeanPostProcessor that instruments {@link TelegramBotExecutor}
 * to record Telegram API call metrics.
 */
public final class TelegramBotExecutorObservabilityBeanPostProcessor implements BeanPostProcessor, PriorityOrdered {
    private static final Logger log = LoggerFactory.getLogger(TelegramBotExecutorObservabilityBeanPostProcessor.class);

    private final MetricsRecorderResolver metricsRecorderResolver;

    public TelegramBotExecutorObservabilityBeanPostProcessor(
            ObjectProvider<MetricsRecorder> metricsRecorderObjectProvider) {
        this.metricsRecorderResolver = new MetricsRecorderResolver(metricsRecorderObjectProvider, log);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!(bean instanceof TelegramBotExecutor)) {
            return bean;
        }

        if (bean instanceof ObservabilityWrapped) {
            return bean;
        }

        Class<?> userClass = ClassUtils.getUserClass(bean);

        boolean finalClass = Modifier.isFinal(userClass.getModifiers());
        boolean finalExecute = isFinalExecuteMethod(userClass);

        if (finalClass || finalExecute) {
            log.warn(
                    "Observability: TelegramBotExecutor bean '{}' ({}) is final (classFinal={}, executeFinal={}); " +
                            "API metrics instrumentation skipped.",
                    beanName, userClass.getName(), finalClass, finalExecute
            );
            return bean;
        }

        ProxyFactory pf = new ProxyFactory();
        pf.setTarget(bean);
        pf.setProxyTargetClass(true);
        pf.setInterfaces(ProxyInterfaces.mergeInterfacesDeep(userClass, ObservabilityWrapped.class));
        pf.addAdvice(new TelegramBotExecutorMetricsInterceptor(metricsRecorderResolver.get()));
        return pf.getProxy();
    }

    private static boolean isFinalExecuteMethod(Class<?> userClass) {
        for (Method m : userClass.getMethods()) {
            if (!"execute".equals(m.getName())) {
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
