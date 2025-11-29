package io.ksilisk.telegrambot.core.dispatcher;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.router.CompositeUpdateRouter;
import io.ksilisk.telegrambot.core.router.UpdateRouter;
import io.ksilisk.telegrambot.core.strategy.CompositeUpdateNoMatchStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class SimpleUpdateDispatcherTest {
    private UpdateRouter router1;
    private UpdateRouter router2;
    private CompositeUpdateNoMatchStrategy noMatchStrategy;
    private SimpleUpdateDispatcher dispatcher;
    private Update update;

    @BeforeEach
    void setUp() {
        router1 = mock(CompositeUpdateRouter.class);
        router2 = mock(UpdateRouter.class);
        noMatchStrategy = mock(CompositeUpdateNoMatchStrategy.class);
        update = mock(Update.class);

        dispatcher = new SimpleUpdateDispatcher(new CompositeUpdateRouter(List.of(router1, router2)), noMatchStrategy);
    }

    @Test
    void shouldRouteUsingFirstSupportingRouter() {
        // router1 supports update
        when(router1.supports(update)).thenReturn(true);
        when(router1.route(update)).thenReturn(true); // successful

        dispatcher.dispatch(update);

        verify(router1).supports(update);
        verify(router1).route(update);
        verifyNoInteractions(router2);
        verifyNoInteractions(noMatchStrategy);
    }

    @Test
    void shouldInvokeNoMatchWhenRouterSupportsButCannotRoute() {
        when(router1.supports(update)).thenReturn(true);
        when(router1.route(update)).thenReturn(false); // couldn't process

        dispatcher.dispatch(update);

        verify(router1).supports(update);
        verify(router1).route(update);
        verify(noMatchStrategy).handle(update);
        verifyNoInteractions(router2);
    }

    @Test
    void shouldUseNextRouterIfFirstDoesNotSupport() {
        when(router1.supports(update)).thenReturn(false);
        when(router2.supports(update)).thenReturn(true);
        when(router2.route(update)).thenReturn(true);

        dispatcher.dispatch(update);

        verify(router1).supports(update);
        verify(router2).supports(update);
        verify(router2).route(update);
        verifyNoInteractions(noMatchStrategy);
    }

    @Test
    void shouldCallNoMatchWhenNoRoutersSupport() {
        when(router1.supports(update)).thenReturn(false);
        when(router2.supports(update)).thenReturn(false);

        dispatcher.dispatch(update);

        verify(router1).supports(update);
        verify(router2).supports(update);
        verify(noMatchStrategy).handle(update);
    }

    @Test
    void shouldStopAfterFirstSupportingRouter() {
        when(router1.supports(update)).thenReturn(true);
        when(router1.route(update)).thenReturn(true);

        dispatcher.dispatch(update);

        verify(router1).supports(update);
        verify(router1).route(update);

        // the second one should be invoked
        verifyNoInteractions(router2);
    }
}
