package io.ksilisk.telegrambot.observability.resolver;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import io.ksilisk.telegrambot.core.handler.update.inline.InlineUpdateHandler;
import io.ksilisk.telegrambot.core.handler.update.message.MessageUpdateHandler;
import io.ksilisk.telegrambot.observability.metrics.TelegramBotChannel;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultTelegramBotChannelResolverTest {
    private final TelegramBotChannelResolver resolver =
            new DefaultTelegramBotChannelResolver();

    @Test
    void shouldResolveCommandChannel() {
        CommandUpdateHandler handler = new CommandUpdateHandler() {
            @Override
            public Set<String> commands() {
                return Set.of("/test");
            }

            @Override
            public void handle(Update update) {
                // noop
            }
        };
        assertEquals(TelegramBotChannel.COMMAND, resolver.resolve(handler));
    }

    @Test
    void shouldResolveCallbackChannel() {
        CallbackUpdateHandler handler = new CallbackUpdateHandler() {
            @Override
            public Set<String> callbacks() {
                return Set.of("test");
            }

            @Override
            public void handle(Update update) {
                // noop
            }
        };
        assertEquals(TelegramBotChannel.CALLBACK, resolver.resolve(handler));
    }

    @Test
    void shouldResolveMessageChannel() {
        MessageUpdateHandler handler = update -> {
        };
        assertEquals(TelegramBotChannel.MESSAGE, resolver.resolve(handler));
    }

    @Test
    void shouldResolveInlineChannel() {
        InlineUpdateHandler handler = update -> {
        };
        assertEquals(TelegramBotChannel.INLINE, resolver.resolve(handler));
    }

    @Test
    void shouldResolveOtherChannelForUnknownHandler() {
        Object handler = new Object();
        assertEquals(TelegramBotChannel.OTHER, resolver.resolve(handler));
    }
}
