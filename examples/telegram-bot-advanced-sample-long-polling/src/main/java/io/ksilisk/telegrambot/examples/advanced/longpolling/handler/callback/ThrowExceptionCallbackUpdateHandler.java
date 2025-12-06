package io.ksilisk.telegrambot.examples.advanced.longpolling.handler.callback;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ThrowExceptionCallbackUpdateHandler implements CallbackUpdateHandler {
    @Override
    public void handle(Update update) {
        throw new IllegalStateException("Some unexpected exception in this callback handler");
    }

    @Override
    public Set<String> callbacks() {
        return Set.of("throw_exception");
    }
}
