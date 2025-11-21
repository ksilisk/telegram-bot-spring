package io.ksilisk.telegrambot.core.registry.handler.command;

import io.ksilisk.telegrambot.core.exception.registry.CommandHandlerAlreadyExists;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultCommandHandlerRegistryTest {
    private DefaultCommandHandlerRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new DefaultCommandHandlerRegistry();
    }

    @Test
    void shouldRegisterSingleCommandHandler() {
        CommandUpdateHandler handler = mock(CommandUpdateHandler.class);

        when(handler.commands()).thenReturn(Set.of("/start"));

        registry.register(handler);

        Optional<UpdateHandler> found = registry.find("/start");

        assertTrue(found.isPresent(), "Handler should be found for registered command");
        assertSame(handler, found.get(), "Returned handler instance should match the registered handler");
    }

    @Test
    void shouldRegisterHandlerForMultipleCommands() {
        CommandUpdateHandler handler = mock(CommandUpdateHandler.class);

        when(handler.commands()).thenReturn(Set.of("/start", "/help", "/ping"));

        registry.register(handler);

        assertSame(handler, registry.find("/start").orElseThrow());
        assertSame(handler, registry.find("/help").orElseThrow());
        assertSame(handler, registry.find("/ping").orElseThrow());
    }

    @Test
    void shouldReturnEmptyForUnknownCommand() {
        Optional<UpdateHandler> found = registry.find("/unknown");

        assertTrue(found.isEmpty(), "Unknown command should return empty Optional");
    }

    @Test
    void shouldThrowWhenRegisteringDuplicateCommand() {
        CommandUpdateHandler handler1 = mock(CommandUpdateHandler.class);
        CommandUpdateHandler handler2 = mock(CommandUpdateHandler.class);

        when(handler1.commands()).thenReturn(Set.of("/dup"));
        when(handler2.commands()).thenReturn(Set.of("/dup"));

        registry.register(handler1);

        CommandHandlerAlreadyExists ex = assertThrows(
                CommandHandlerAlreadyExists.class,
                () -> registry.register(handler2),
                "Duplicate command registration should throw CommandHandlerAlreadyExists"
        );

        assertTrue(
                ex.getMessage().contains("/dup"),
                "Exception message should include the conflicting command"
        );
    }

    @Test
    void shouldAllowDifferentHandlersForDifferentCommands() {
        CommandUpdateHandler handler1 = mock(CommandUpdateHandler.class);
        CommandUpdateHandler handler2 = mock(CommandUpdateHandler.class);

        when(handler1.commands()).thenReturn(Set.of("/one"));
        when(handler2.commands()).thenReturn(Set.of("/two"));

        registry.register(handler1);
        registry.register(handler2);

        assertSame(handler1, registry.find("/one").orElseThrow());
        assertSame(handler2, registry.find("/two").orElseThrow());
    }
}
