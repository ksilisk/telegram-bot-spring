package io.ksilisk.telegrambot.core.delivery;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.dispatcher.UpdateDispatcher;
import io.ksilisk.telegrambot.core.handler.exception.CompositeUpdateExceptionHandler;
import io.ksilisk.telegrambot.core.interceptor.CompositeUpdateInterceptor;
import io.ksilisk.telegrambot.core.properties.DeliveryProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DefaultUpdateDeliveryTest {
    private UpdateDispatcher updateDispatcher;
    private ExecutorService executorService;
    private DeliveryProperties deliveryProperties;
    private CompositeUpdateInterceptor compositeUpdateInterceptor;
    private CompositeUpdateExceptionHandler exceptionHandler;

    private DefaultUpdateDelivery delivery;

    @BeforeEach
    void setUp() {
        updateDispatcher = mock(UpdateDispatcher.class);
        executorService = mock(ExecutorService.class);
        deliveryProperties = mock(DeliveryProperties.class);
        compositeUpdateInterceptor = mock(CompositeUpdateInterceptor.class);
        exceptionHandler = mock(CompositeUpdateExceptionHandler.class);

        when(deliveryProperties.getShutdownTimeout()).thenReturn(Duration.ofMillis(100));

        delivery = new DefaultUpdateDelivery(
                updateDispatcher,
                executorService,
                deliveryProperties,
                compositeUpdateInterceptor,
                exceptionHandler
        );
    }

    // --------------------------------------------------
    // deliver()
    // --------------------------------------------------

    @Test
    void shouldSubmitTaskForEachPolledUpdateAndDispatchWhenInterceptedNotNull() throws Exception {
        Update update1 = mock(Update.class);
        Update update2 = mock(Update.class);

        List<Runnable> submittedTasks = new ArrayList<>();

        // Capture submitted Runnable tasks instead of actually running them on a real executor
        when(executorService.submit(any(Runnable.class))).thenAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            submittedTasks.add(task);
            return mock(Future.class);
        });

        when(compositeUpdateInterceptor.intercept(update1)).thenReturn(update1); // intercepted but same instance is fine
        when(compositeUpdateInterceptor.intercept(update2)).thenReturn(update2);

        delivery.deliver(List.of(update1, update2));

        // Two tasks should be submitted
        assertEquals(2, submittedTasks.size(), "Each update should result in a submitted task");

        // Execute tasks synchronously to simulate processing
        submittedTasks.forEach(Runnable::run);

        // Interceptor must be invoked for each update
        verify(compositeUpdateInterceptor).intercept(update1);
        verify(compositeUpdateInterceptor).intercept(update2);

        // Dispatcher must be called for each update
        // NOTE: current implementation dispatches the original update, not the intercepted one.
        verify(updateDispatcher).dispatch(update1);
        verify(updateDispatcher).dispatch(update2);

        // No exception handling should be triggered in this happy path
        verifyNoInteractions(exceptionHandler);
    }

    @Test
    void shouldNotDispatchWhenInterceptorReturnsNull() throws Exception {
        Update update = mock(Update.class);
        List<Runnable> submittedTasks = new ArrayList<>();

        when(executorService.submit(any(Runnable.class))).thenAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            submittedTasks.add(task);
            return mock(Future.class);
        });

        // Interceptor decides to skip this update
        when(compositeUpdateInterceptor.intercept(update)).thenReturn(null);

        delivery.deliver(List.of(update));

        assertEquals(1, submittedTasks.size());

        submittedTasks.get(0).run();

        verify(compositeUpdateInterceptor).intercept(update);
        verifyNoInteractions(updateDispatcher);
        verifyNoInteractions(exceptionHandler);
    }

    @Test
    void shouldDoNothingWhenPolledUpdatesListIsEmpty() {
        delivery.deliver(List.of());

        // No tasks should be submitted in this case
        verifyNoInteractions(executorService);
        verifyNoInteractions(compositeUpdateInterceptor);
        verifyNoInteractions(updateDispatcher);
        verifyNoInteractions(exceptionHandler);
    }

    @Test
    void shouldInvokeExceptionHandlerWhenInterceptorThrows() throws Exception {
        Update update = mock(Update.class);
        List<Runnable> submittedTasks = new ArrayList<>();

        when(executorService.submit(any(Runnable.class))).thenAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            submittedTasks.add(task);
            return mock(Future.class);
        });

        RuntimeException ex = new RuntimeException("interceptor failed");
        when(compositeUpdateInterceptor.intercept(update)).thenThrow(ex);

        delivery.deliver(List.of(update));

        submittedTasks.get(0).run();

        verify(compositeUpdateInterceptor).intercept(update);
        verifyNoInteractions(updateDispatcher);
        verify(exceptionHandler).handle(ex, update);
    }

    @Test
    void shouldInvokeExceptionHandlerWhenDispatcherThrows() throws Exception {
        Update update = mock(Update.class);
        List<Runnable> submittedTasks = new ArrayList<>();

        when(executorService.submit(any(Runnable.class))).thenAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            submittedTasks.add(task);
            return mock(Future.class);
        });

        when(compositeUpdateInterceptor.intercept(update)).thenReturn(update);

        RuntimeException ex = new RuntimeException("dispatch failed");
        doThrow(ex).when(updateDispatcher).dispatch(update);

        delivery.deliver(List.of(update));

        submittedTasks.get(0).run();

        verify(compositeUpdateInterceptor).intercept(update);
        verify(updateDispatcher).dispatch(update);
        verify(exceptionHandler).handle(ex, update);
    }

    // --------------------------------------------------
    // close()
    // --------------------------------------------------

    @Test
    void shouldShutdownGracefullyWhenExecutorTerminatesInTime() throws Exception {
        when(executorService.awaitTermination(100L, TimeUnit.MILLISECONDS)).thenReturn(true);

        delivery.stop();

        verify(executorService).shutdown();
        verify(executorService).awaitTermination(100L, TimeUnit.MILLISECONDS);
        verify(executorService, never()).shutdownNow();
    }

    @Test
    void shouldForceShutdownWhenExecutorDoesNotTerminateInTime() throws Exception {
        when(executorService.awaitTermination(100L, TimeUnit.MILLISECONDS)).thenReturn(false);

        delivery.stop();

        verify(executorService).shutdown();
        verify(executorService).awaitTermination(100L, TimeUnit.MILLISECONDS);
        verify(executorService).shutdownNow();
    }

    @Test
    void shouldForceShutdownAndRestoreInterruptFlagWhenInterruptedDuringAwaitTermination() throws Exception {
        when(executorService.awaitTermination(100L, TimeUnit.MILLISECONDS))
                .thenThrow(new InterruptedException("test interruption"));

        // Make sure interrupt flag is clear before
        Thread.interrupted(); // clears current thread's interrupt status

        delivery.stop();

        // Executor shutdown sequence
        verify(executorService).shutdown();
        verify(executorService).awaitTermination(100L, TimeUnit.MILLISECONDS);
        verify(executorService).shutdownNow();

        // Interrupt flag should be set again by the code
        assertTrue(Thread.currentThread().isInterrupted(),
                "Current thread should be interrupted after InterruptedException is caught");

        // Clear interrupt status so this test does not affect others
        Thread.interrupted();
    }
}
