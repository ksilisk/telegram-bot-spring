package io.ksilisk.telegrambot.observability.bpp;

import io.ksilisk.telegrambot.core.delivery.DeliveryThreadPoolExecutorFactory;
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
import java.util.concurrent.ThreadPoolExecutor;

/**
 * BeanPostProcessor that instruments {@link DeliveryThreadPoolExecutorFactory}
 * to register gauges for the delivery {@link ThreadPoolExecutor}.
 */
public final class DeliveryThreadPoolExecutorFactoryObservabilityBeanPostProcessor
        implements BeanPostProcessor, PriorityOrdered {
    private static final Logger log =
            LoggerFactory.getLogger(DeliveryThreadPoolExecutorFactoryObservabilityBeanPostProcessor.class);

    private final MetricsRecorderResolver metricsRecorderResolver;

    public DeliveryThreadPoolExecutorFactoryObservabilityBeanPostProcessor(
            ObjectProvider<MetricsRecorder> recorderProvider) {
        this.metricsRecorderResolver = new MetricsRecorderResolver(recorderProvider, log);
    }


    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!(bean instanceof io.ksilisk.telegrambot.core.delivery.DeliveryThreadPoolExecutorFactory)) {
            return bean;
        }
        if (bean instanceof ObservabilityWrapped) {
            return bean;
        }

        Class<?> userClass = ClassUtils.getUserClass(bean);

        boolean finalClass = Modifier.isFinal(userClass.getModifiers());
        boolean finalExecute = isFinalBuildMethod(userClass);

        if (finalClass || finalExecute) {
            log.warn("Observability: DeliveryThreadPoolExecutorFactory bean '{}' ({}) " +
                            "is final (classFinal={}, executeFinal={}); delivery pool metrics skipped.",
                    beanName, userClass.getName(), finalClass, finalExecute);
            return bean;
        }

        MetricsRecorder metrics = metricsRecorderResolver.get();

        ProxyFactory pf = new ProxyFactory();
        pf.setTarget(bean);
        pf.setProxyTargetClass(true);
        pf.setInterfaces(ProxyInterfaces.mergeInterfacesDeep(userClass, ObservabilityWrapped.class));
        pf.addAdvice(new DeliveryThreadPoolExecutorMetricsInterceptor(metrics));

        return pf.getProxy();
    }

    private static boolean isFinalBuildMethod(Class<?> userClass) {
        for (Method m : userClass.getMethods()) {
            if (!m.getName().equals("buildThreadPoolExecutor")) {
                continue;
            }
            if (m.getParameterCount() != 0) {
                continue;
            }
            if (Modifier.isFinal(m.getModifiers())) {
                return true;
            }
        }
        return false;
    }
}
