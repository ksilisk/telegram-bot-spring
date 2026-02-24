package io.ksilisk.telegrambot.longpolling.poller;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import io.ksilisk.telegrambot.core.delivery.UpdateDelivery;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.longpolling.properties.LongPollingProperties;
import io.ksilisk.telegrambot.longpolling.store.OffsetStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

class DefaultUpdatePollerTest {
    private OffsetStore offsetStore;
    private TelegramBotExecutor telegramBotExecutor;
    private UpdateDelivery updateDelivery;
    private LongPollingProperties properties;

    @BeforeEach
    void setUp() {
        offsetStore = mock(OffsetStore.class);
        telegramBotExecutor = mock(TelegramBotExecutor.class);
        updateDelivery = mock(UpdateDelivery.class);
        properties = mock(LongPollingProperties.class);

        // Common defaults to avoid NPEs
        when(properties.getTimeout()).thenReturn(Duration.ofSeconds(1));
        when(properties.getLimit()).thenReturn(100);
        when(properties.getRetryDelay()).thenReturn(Duration.ZERO);
        when(properties.getShutdownTimeout()).thenReturn(Duration.ofSeconds(1));
        when(properties.getBackoffEnabled()).thenReturn(false);
        when(offsetStore.read()).thenReturn(0);
    }

    private DefaultUpdatePoller createPoller() {
        return new DefaultUpdatePoller(offsetStore, telegramBotExecutor, updateDelivery, properties);
    }


    private void setPrivateBooleanField(Object target, String fieldName, boolean value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.setBoolean(target, value);
    }

    // --------------------------------------------------
    // drainPendingUpdates()
    // --------------------------------------------------

    @Test
    void shouldWriteOffsetWhenDrainingPendingUpdates() throws Exception {
        DefaultUpdatePoller poller = createPoller();

        GetUpdatesResponse response = mock(GetUpdatesResponse.class);
        Update u1 = mock(Update.class);
        Update u2 = mock(Update.class);

        when(telegramBotExecutor.execute(any(GetUpdates.class))).thenReturn(response);
        when(response.updates()).thenReturn(List.of(u1, u2));
        when(u1.updateId()).thenReturn(10);
        when(u2.updateId()).thenReturn(20);

        // Call private drainPendingUpdates() directly (white-box test)
        Method drain = DefaultUpdatePoller.class.getDeclaredMethod("drainPendingUpdates");
        drain.setAccessible(true);
        drain.invoke(poller);

        // Expect offsetStore.write(lastUpdateId)
        verify(offsetStore).write(20);
    }

    @Test
    void shouldSwallowExceptionWhenDrainingPendingUpdatesFails() throws Exception {
        DefaultUpdatePoller poller = createPoller();

        when(telegramBotExecutor.execute(any(GetUpdates.class)))
                .thenThrow(new RuntimeException("network error"));

        Method drain = DefaultUpdatePoller.class.getDeclaredMethod("drainPendingUpdates");
        drain.setAccessible(true);

        // Method must not propagate exception
        assertDoesNotThrow(() -> {
            try {
                drain.invoke(poller);
            } catch (Exception e) {
                // unwrap reflection InvocationTargetException
                throw new RuntimeException(e.getCause());
            }
        });

        // No offset should be written in case of failure
        verifyNoInteractions(offsetStore);
    }

    // --------------------------------------------------
    // runLoop()
    // --------------------------------------------------

    @Test
    void shouldDeliverUpdatesAndUpdateOffsetInBackgroundThread() throws Exception {
        DefaultUpdatePoller poller = createPoller();

        when(properties.getDropPendingOnStart()).thenReturn(false);

        GetUpdatesResponse response = mock(GetUpdatesResponse.class);
        Update u1 = mock(Update.class);
        Update u2 = mock(Update.class);

        when(u1.updateId()).thenReturn(10);
        when(u2.updateId()).thenReturn(20);

        when(telegramBotExecutor.execute(any(GetUpdates.class))).thenReturn(response);
        when(response.isOk()).thenReturn(true);
        when(response.updates()).thenReturn(List.of(u1, u2));

        // Latch to know when deliver() was called
        CountDownLatch latch = new CountDownLatch(1);
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(updateDelivery).deliver(anyList());

        // Mark poller as running
        setPrivateBooleanField(poller, "running", true);

        Method runLoop = DefaultUpdatePoller.class.getDeclaredMethod("runLoop");
        runLoop.setAccessible(true);

        // Run loop in a separate thread to allow us to stop it from the test
        Thread t = new Thread(() -> {
            try {
                runLoop.invoke(poller);
            } catch (Exception e) {
                // rethrow as unchecked to fail the test if something goes wrong
                throw new RuntimeException(e);
            }
        }, "test-poller-thread");

        t.start();

        // Wait until deliver() is called
        assertTrue(latch.await(2, java.util.concurrent.TimeUnit.SECONDS),
                "UpdateDelivery.deliver should be invoked at least once");

        // Now stop the loop by setting running = false
        setPrivateBooleanField(poller, "running", false);

        // Wait for the loop thread to finish
        t.join(2000);

        // At least one successful delivery and offset update should have happened
        verify(updateDelivery, atLeastOnce()).deliver(anyList());
        verify(offsetStore, atLeastOnce()).write(20);
    }

    // --------------------------------------------------
    // start()/stop()/close() – smoke tests
    // --------------------------------------------------

    @Test
    void startAndStopShouldNotThrow() {
        DefaultUpdatePoller poller = createPoller();

        // We do not care about inner loop behavior here, just smoke-test lifecycle methods.
        assertDoesNotThrow(poller::start);
        assertDoesNotThrow(poller::stop);
        assertDoesNotThrow(poller::close);
    }

    @Test
    void multipleStartCallsShouldNotSubmitMultipleLoops() {
        DefaultUpdatePoller poller = spy(createPoller());

        // We cannot easily spy on internal ExecutorService, so we rely on contract:
        // second start() should just log a warning and not throw.
        poller.start();
        poller.start(); // should not break anything

        poller.stop();
    }

    // --------------------------------------------------
    // computeRetryDelay()
    // --------------------------------------------------

    @Test
    void computeRetryDelayShouldGrowExponentiallyWithFailureCount() throws Exception {
        when(properties.getBackoffEnabled()).thenReturn(true);
        when(properties.getRetryDelay()).thenReturn(Duration.ofMillis(100));
        when(properties.getBackoffMaxDelay()).thenReturn(Duration.ofMinutes(1));
        when(properties.getBackoffMultiplier()).thenReturn(2.0);

        DefaultUpdatePoller poller = createPoller();
        Method method = DefaultUpdatePoller.class.getDeclaredMethod("computeRetryDelay", int.class);
        method.setAccessible(true);

        long delay0 = (long) method.invoke(poller, 0);
        long delay1 = (long) method.invoke(poller, 1);
        long delay2 = (long) method.invoke(poller, 2);

        assertTrue(delay0 >= 100L, "First retry should be at least base delay");
        assertTrue(delay1 >= 200L, "Second retry should be at least 2x base delay");
        assertTrue(delay2 >= 400L, "Third retry should be at least 4x base delay");
    }

    @Test
    void computeRetryDelayShouldBeCappedAtMaxDelay() throws Exception {
        when(properties.getBackoffEnabled()).thenReturn(true);
        when(properties.getRetryDelay()).thenReturn(Duration.ofMillis(100));
        when(properties.getBackoffMaxDelay()).thenReturn(Duration.ofMillis(300));
        when(properties.getBackoffMultiplier()).thenReturn(2.0);

        DefaultUpdatePoller poller = createPoller();
        Method method = DefaultUpdatePoller.class.getDeclaredMethod("computeRetryDelay", int.class);
        method.setAccessible(true);

        long delay = (long) method.invoke(poller, 10);

        assertTrue(delay <= 300L, "Delay should be capped at backoffMaxDelay");
    }

    @Test
    void computeRetryDelayShouldReturnFixedDelayWhenBackoffDisabled() throws Exception {
        when(properties.getBackoffEnabled()).thenReturn(false);
        when(properties.getRetryDelay()).thenReturn(Duration.ofMillis(500));

        DefaultUpdatePoller poller = createPoller();
        Method method = DefaultUpdatePoller.class.getDeclaredMethod("computeRetryDelay", int.class);
        method.setAccessible(true);

        long delay0 = (long) method.invoke(poller, 0);
        long delay5 = (long) method.invoke(poller, 5);

        assertEquals(500L, delay0);
        assertEquals(500L, delay5);
    }

    @Test
    void runLoopShouldRecoverAndResetBackoffAfterSuccessfulResponse() throws Exception {
        when(properties.getDropPendingOnStart()).thenReturn(false);
        when(properties.getBackoffEnabled()).thenReturn(true);
        when(properties.getBackoffMaxDelay()).thenReturn(Duration.ofSeconds(60));
        when(properties.getBackoffMultiplier()).thenReturn(2.0);

        GetUpdatesResponse response = mock(GetUpdatesResponse.class);
        Update update = mock(Update.class);
        when(update.updateId()).thenReturn(42);
        when(response.isOk()).thenReturn(true);
        when(response.updates()).thenReturn(List.of(update));

        CountDownLatch deliverLatch = new CountDownLatch(1);
        doAnswer(invocation -> {
            deliverLatch.countDown();
            return null;
        }).when(updateDelivery).deliver(anyList());

        // First call throws, second call succeeds – verifies backoff reset path is reached
        when(telegramBotExecutor.execute(any(GetUpdates.class)))
                .thenThrow(new RuntimeException("transient error"))
                .thenReturn(response);

        DefaultUpdatePoller poller = createPoller();
        setPrivateBooleanField(poller, "running", true);

        Method runLoop = DefaultUpdatePoller.class.getDeclaredMethod("runLoop");
        runLoop.setAccessible(true);

        Thread t = new Thread(() -> {
            try {
                runLoop.invoke(poller);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, "test-backoff-reset-thread");

        t.start();

        assertTrue(deliverLatch.await(3, java.util.concurrent.TimeUnit.SECONDS),
                "UpdateDelivery.deliver should be invoked after recovering from failure");

        setPrivateBooleanField(poller, "running", false);
        t.join(2000);

        verify(updateDelivery, atLeastOnce()).deliver(anyList());
        verify(offsetStore, atLeastOnce()).write(42);
    }
}
