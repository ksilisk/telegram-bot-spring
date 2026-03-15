package io.ksilisk.telegrambot.core.executor.retry.impl;

import com.pengrad.telegrambot.request.BaseRequest;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

class FixedRetryDelayStrategyTest {
    @Test
    void shouldAlwaysReturnConfiguredDelay() {
        Duration delay = Duration.ofSeconds(3);
        FixedRetryDelayStrategy strategy = new FixedRetryDelayStrategy(delay);
        BaseRequest<?, ?> request = mock(BaseRequest.class);
        RuntimeException error = new RuntimeException("boom");

        assertSame(delay, strategy.nextDelay(request, error, 1));
        assertSame(delay, strategy.nextDelay(request, error, 5));
    }
}
