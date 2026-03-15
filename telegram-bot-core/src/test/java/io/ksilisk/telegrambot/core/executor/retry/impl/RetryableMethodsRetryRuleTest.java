package io.ksilisk.telegrambot.core.executor.retry.impl;

import com.pengrad.telegrambot.request.BaseRequest;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RetryableMethodsRetryRuleTest {
    @Test
    void shouldAllowRetryForConfiguredMethod() {
        RetryableMethodsRetryRule rule =
                new RetryableMethodsRetryRule(Set.of("sendMessage", "editMessageText"));
        BaseRequest<?, ?> request = mock(BaseRequest.class);

        when(request.getMethod()).thenReturn("sendMessage");

        assertTrue(rule.shouldRetry(request, new RuntimeException("boom"), 1));
    }

    @Test
    void shouldRejectRetryForMethodOutsideAllowList() {
        RetryableMethodsRetryRule rule =
                new RetryableMethodsRetryRule(Set.of("sendMessage", "editMessageText"));
        BaseRequest<?, ?> request = mock(BaseRequest.class);

        when(request.getMethod()).thenReturn("deleteWebhook");

        assertFalse(rule.shouldRetry(request, new RuntimeException("boom"), 1));
    }

    @Test
    void shouldRejectRetryWhenMethodIsNull() {
        RetryableMethodsRetryRule rule =
                new RetryableMethodsRetryRule(Set.of("sendMessage"));
        BaseRequest<?, ?> request = mock(BaseRequest.class);

        when(request.getMethod()).thenReturn(null);

        assertFalse(rule.shouldRetry(request, new RuntimeException("boom"), 1));
    }

    @Test
    void shouldRejectRetryWhenMethodIsBlank() {
        RetryableMethodsRetryRule rule =
                new RetryableMethodsRetryRule(Set.of("sendMessage"));
        BaseRequest<?, ?> request = mock(BaseRequest.class);

        when(request.getMethod()).thenReturn("   ");

        assertFalse(rule.shouldRetry(request, new RuntimeException("boom"), 1));
    }
}
