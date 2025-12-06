package io.ksilisk.telegrambot.examples.advanced.longpolling.interceptor;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.interceptor.UpdateInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FilterMessageReactionUpdateInterceptor implements UpdateInterceptor {
    private static final Logger log = LoggerFactory.getLogger(FilterMessageReactionUpdateInterceptor.class);

    @Override
    public Update intercept(Update input) {
        if (input.messageReaction() != null) {
            log.info("Handled update with a message reaction. Skip it. Update: {}", input);
            return null;
        }
        return input;
    }
}
