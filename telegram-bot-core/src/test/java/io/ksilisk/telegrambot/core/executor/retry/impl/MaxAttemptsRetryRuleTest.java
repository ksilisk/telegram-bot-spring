package io.ksilisk.telegrambot.core.executor.retry.impl;

import com.pengrad.telegrambot.request.BaseRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class MaxAttemptsRetryRuleTest {
    @Test
    void shouldAllowRetryBeforeMaxAttempts() {
        MaxAttemptsRetryRule rule = new MaxAttemptsRetryRule(3);
        BaseRequest<?, ?> request = mock(BaseRequest.class);

        assertTrue(rule.shouldRetry(request, new RuntimeException("boom"), 1));
        assertTrue(rule.shouldRetry(request, new RuntimeException("boom"), 2));
    }

    @Test
    void shouldRejectRetryWhenAttemptReachesOrExceedsLimit() {
        MaxAttemptsRetryRule rule = new MaxAttemptsRetryRule(3);
        BaseRequest<?, ?> request = mock(BaseRequest.class);

        assertFalse(rule.shouldRetry(request, new RuntimeException("boom"), 3));
        assertFalse(rule.shouldRetry(request, new RuntimeException("boom"), 4));
    }
}
