package io.ksilisk.telegrambot.longpolling.retry;

import com.pengrad.telegrambot.request.BaseRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetUpdatesExcludedRetryRuleTest {
    @Test
    void shouldRejectRetryForGetUpdatesMethod() {
        GetUpdatesExcludedRetryRule rule = new GetUpdatesExcludedRetryRule();
        BaseRequest<?, ?> request = mock(BaseRequest.class);

        when(request.getMethod()).thenReturn("getUpdates");

        assertFalse(rule.shouldRetry(request, new RuntimeException("boom"), 1));
    }

    @Test
    void shouldAllowRetryForAnyOtherMethod() {
        GetUpdatesExcludedRetryRule rule = new GetUpdatesExcludedRetryRule();
        BaseRequest<?, ?> request = mock(BaseRequest.class);

        when(request.getMethod()).thenReturn("sendMessage");

        assertTrue(rule.shouldRetry(request, new RuntimeException("boom"), 1));
    }

    @Test
    void shouldAllowRetryWhenMethodIsNull() {
        GetUpdatesExcludedRetryRule rule = new GetUpdatesExcludedRetryRule();
        BaseRequest<?, ?> request = mock(BaseRequest.class);

        when(request.getMethod()).thenReturn(null);

        assertTrue(rule.shouldRetry(request, new RuntimeException("boom"), 1));
    }
}
