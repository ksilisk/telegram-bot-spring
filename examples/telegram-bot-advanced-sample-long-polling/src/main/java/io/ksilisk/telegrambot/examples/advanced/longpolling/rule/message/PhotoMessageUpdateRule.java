package io.ksilisk.telegrambot.examples.advanced.longpolling.rule.message;

import com.pengrad.telegrambot.model.Message;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.matcher.Matcher;
import io.ksilisk.telegrambot.core.rule.MessageUpdateRule;
import io.ksilisk.telegrambot.examples.advanced.longpolling.handler.message.PhotoMessageUpdateHandler;
import org.springframework.stereotype.Component;

@Component
public class PhotoMessageUpdateRule implements MessageUpdateRule {
    private final PhotoMessageUpdateHandler updateHandler;

    public PhotoMessageUpdateRule(PhotoMessageUpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
    }

    @Override
    public Matcher<Message> matcher() {
        return m -> m.photo() != null && m.photo().length > 0;
    }

    @Override
    public UpdateHandler handler() {
        return updateHandler;
    }
}
