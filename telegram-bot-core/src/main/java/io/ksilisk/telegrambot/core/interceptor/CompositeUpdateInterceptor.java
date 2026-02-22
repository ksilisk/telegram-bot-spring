package io.ksilisk.telegrambot.core.interceptor;

import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CompositeUpdateInterceptor implements UpdateInterceptor {
    private static final Logger log = LoggerFactory.getLogger(CompositeUpdateInterceptor.class);

    private final List<UpdateInterceptor> delegates;

    public CompositeUpdateInterceptor(List<UpdateInterceptor> delegates) {
        this.delegates = delegates;
    }

    @Override
    public Update intercept(Update input) {
        Update current = input;
        for (UpdateInterceptor updateInterceptor : delegates) {
            log.debug("Invoking update interceptor: {}", updateInterceptor.getClass().getSimpleName());
            current = updateInterceptor.intercept(current);
            if (current == null) {
                log.debug("Update (id={}) was skipped by {}", input.updateId(), updateInterceptor.getClass().getSimpleName());
                return null;
            }
        }
        return current;
    }
}
