package io.ksilisk.telegrambot.autoconfigure.adapter;

import io.ksilisk.telegrambot.core.order.CoreOrdered;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Proxy;

/**
 * Adapts {@link CoreOrdered} components to respect Spring's ordering metadata.
 * <p>
 * If a component does <strong>not</strong> explicitly override {@link CoreOrdered#getOrder()},
 * and it is annotated with {@link org.springframework.core.annotation.Order} or implements
 * {@link org.springframework.core.Ordered}, this adapter provides a proxy that supplies the
 * Spring-defined order value through {@link CoreOrdered#getOrder()}.
 * <p>
 * If the component overrides {@code getOrder()}, the explicit value always takes precedence and
 * no adaptation is applied.
 * <p>
 * This class is internal to the Spring Boot starter and is not intended for direct use.
 */
public final class SpringCoreOrderAdapter {
    private SpringCoreOrderAdapter() {
        throw new IllegalStateException("Utility class shouldn't be created");
    }

    @SuppressWarnings("unchecked")
    public static <T extends CoreOrdered> T adaptIfNecessary(T bean) {
        Integer springOrder = resolveSpringOrder(bean);
        if (springOrder == null) {
            return bean;
        }

        int coreOrder = bean.getOrder();

        if (coreOrder != 0) {
            return bean;
        }

        Class<?>[] interfaces = bean.getClass().getInterfaces();
        if (interfaces.length == 0) {
            return bean;
        }

        return (T) Proxy.newProxyInstance(
                bean.getClass().getClassLoader(),
                interfaces,
                (proxy, method, args) -> {
                    if (method.getName().equals("getOrder") && method.getParameterCount() == 0) {
                        return springOrder;
                    }
                    return method.invoke(bean, args);
                }
        );
    }

    private static Integer resolveSpringOrder(Object bean) {
        if (bean instanceof Ordered ordered) {
            return ordered.getOrder();
        }

        Class<?> userClass = ClassUtils.getUserClass(bean.getClass());
        Order ann = AnnotationUtils.findAnnotation(userClass, Order.class);
        if (ann != null) {
            return ann.value();
        }

        return null;
    }
}
