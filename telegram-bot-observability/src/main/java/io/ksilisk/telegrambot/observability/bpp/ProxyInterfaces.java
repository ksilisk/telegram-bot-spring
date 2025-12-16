package io.ksilisk.telegrambot.observability.bpp;

import java.util.LinkedHashSet;
import java.util.Set;

final class ProxyInterfaces {
    static Class<?>[] mergeInterfacesDeep(Class<?> userClass, Class<?> extra) {
        Set<Class<?>> acc = new LinkedHashSet<>();
        collectInterfacesDeep(userClass, acc);
        acc.add(extra);
        return acc.toArray(Class<?>[]::new);
    }

    private static void collectInterfacesDeep(Class<?> type, Set<Class<?>> acc) {
        if (type == null || type == Object.class) {
            return;
        }
        for (Class<?> itf : type.getInterfaces()) {
            collectInterfaceGraph(itf, acc);
        }
        collectInterfacesDeep(type.getSuperclass(), acc);
    }

    private static void collectInterfaceGraph(Class<?> itf, Set<Class<?>> acc) {
        if (!acc.add(itf)) {
            return;
        }
        for (Class<?> parent : itf.getInterfaces()) {
            collectInterfaceGraph(parent, acc);
        }
    }
}
