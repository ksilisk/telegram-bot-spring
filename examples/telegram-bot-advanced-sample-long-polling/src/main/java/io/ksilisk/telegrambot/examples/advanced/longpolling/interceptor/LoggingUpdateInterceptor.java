package io.ksilisk.telegrambot.examples.advanced.longpolling.interceptor;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.interceptor.UpdateInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingUpdateInterceptor implements UpdateInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LoggingUpdateInterceptor.class);

    @Override
    public Update intercept(Update input) {
        log.info("Handled update: {}", input);
        return input;
    }
}
