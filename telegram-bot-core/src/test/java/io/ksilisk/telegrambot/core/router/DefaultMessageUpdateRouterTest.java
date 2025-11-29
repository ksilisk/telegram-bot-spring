package io.ksilisk.telegrambot.core.router;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.registry.handler.command.CommandHandlerRegistry;
import io.ksilisk.telegrambot.core.registry.rule.message.MessageUpdateRuleRegistry;
import io.ksilisk.telegrambot.core.router.detector.CommandDetector;
import io.ksilisk.telegrambot.core.router.impl.DefaultMessageUpdateRouter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultMessageUpdateRouterTest {
    private CommandHandlerRegistry commandRegistry;
    private MessageUpdateRuleRegistry messageRuleRegistry;
    private CommandDetector commandDetector;
    private DefaultMessageUpdateRouter router;

    private Update update;
    private Message message;

    @BeforeEach
    void setUp() {
        commandRegistry = mock(CommandHandlerRegistry.class);
        messageRuleRegistry = mock(MessageUpdateRuleRegistry.class);
        commandDetector = mock(CommandDetector.class);

        router = new DefaultMessageUpdateRouter(commandRegistry, messageRuleRegistry, commandDetector);

        update = mock(Update.class);
        message = mock(Message.class);

        when(update.message()).thenReturn(message);
    }

    // --------------------------------------------------
    // supports()
    // --------------------------------------------------

    @Test
    void shouldSupportUpdateWhenMessageIsPresent() {
        when(update.message()).thenReturn(message);

        assertTrue(router.supports(update));
    }

    @Test
    void shouldNotSupportUpdateWhenMessageIsNull() {
        when(update.message()).thenReturn(null);

        assertFalse(router.supports(update));
    }

    // --------------------------------------------------
    // route(): COMMAND handling
    // --------------------------------------------------

    @Test
    void shouldRouteCommandWhenDetected() {
        when(message.text()).thenReturn("/start");
        when(commandDetector.detectCommand("/start")).thenReturn(Optional.of("/start"));

        UpdateHandler handler = mock(UpdateHandler.class);
        when(commandRegistry.find("/start")).thenReturn(Optional.of(handler));

        boolean result = router.route(update);

        assertTrue(result, "Command should be routed successfully");
        verify(handler).handle(update);
    }

    @Test
    void shouldReturnFalseWhenCommandDetectedButNoCommandHandlerRegistered() {
        when(message.text()).thenReturn("/start");
        when(commandDetector.detectCommand("/start")).thenReturn(Optional.of("/start"));

        when(commandRegistry.find("/start")).thenReturn(Optional.empty());

        boolean result = router.route(update);

        assertFalse(result, "Should return false when command handler is missing");
    }

    // --------------------------------------------------
    // route(): MESSAGE (non-command) handling
    // --------------------------------------------------

    @Test
    void shouldRouteMessageWhenNoCommandDetectedAndMessageRuleExists() {
        when(message.text()).thenReturn("hello");
        when(commandDetector.detectCommand("hello")).thenReturn(Optional.empty());

        UpdateHandler handler = mock(UpdateHandler.class);
        when(messageRuleRegistry.find(message)).thenReturn(Optional.of(handler));

        boolean result = router.route(update);

        assertTrue(result, "Message routed successfully");
        verify(handler).handle(update);
    }

    @Test
    void shouldReturnFalseWhenNoCommandDetectedAndNoMessageHandlerExists() {
        when(message.text()).thenReturn("hello");
        when(commandDetector.detectCommand("hello")).thenReturn(Optional.empty());

        when(messageRuleRegistry.find(message)).thenReturn(Optional.empty());

        boolean result = router.route(update);

        assertFalse(result, "Should return false when no matching message rule exists");
    }

    // --------------------------------------------------
    // edge cases
    // --------------------------------------------------

    @Test
    void shouldTreatNullTextAsEmptyString() {
        when(message.text()).thenReturn(null);
        when(commandDetector.detectCommand("")).thenReturn(Optional.empty());
        when(messageRuleRegistry.find(message)).thenReturn(Optional.empty());

        boolean result = router.route(update);

        assertFalse(result, "Routing should handle null text safely");
        verify(commandDetector).detectCommand("");
    }

    @Test
    void shouldPreferCommandOverMessageRuleWhenBothApplicable() {
        // If detector returns a command, router MUST NOT call messageRuleRegistry
        when(message.text()).thenReturn("/start hello");
        when(commandDetector.detectCommand("/start hello")).thenReturn(Optional.of("/start"));

        UpdateHandler handler = mock(UpdateHandler.class);
        when(commandRegistry.find("/start")).thenReturn(Optional.of(handler));

        boolean result = router.route(update);

        assertTrue(result);
        verify(handler).handle(update);

        verifyNoInteractions(messageRuleRegistry);
    }

    @Test
    void shouldNotCallHandlerIfSupportsIsFalse() {
        // This test ensures user code doesn't mistakenly call route() when supports() is false.
        when(update.message()).thenReturn(null);

        boolean result = router.route(update);

        assertFalse(result);
    }
}
