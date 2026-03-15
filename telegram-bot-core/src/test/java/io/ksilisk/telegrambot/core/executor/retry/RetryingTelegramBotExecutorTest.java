package io.ksilisk.telegrambot.core.executor.retry;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import io.ksilisk.telegrambot.core.exception.request.TelegramRequestException;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.retry.impl.CompositeRetryRule;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class RetryingTelegramBotExecutorTest {
    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    void shouldReturnResponseWithoutRetryWhenFirstAttemptSucceeds() {
        TelegramBotExecutor delegate = mock(TelegramBotExecutor.class);
        CompositeRetryRule retryRule = mock(CompositeRetryRule.class);
        RetryDelayStrategy delayStrategy = mock(RetryDelayStrategy.class);
        RetryingTelegramBotExecutor executor =
                new RetryingTelegramBotExecutor(delegate, retryRule, delayStrategy);
        BaseRequest request = mock(BaseRequest.class);
        BaseResponse response = mock(BaseResponse.class);

        when(delegate.execute(request)).thenReturn(response);

        BaseResponse actual = executor.execute(request);

        assertSame(response, actual);
        verify(delegate).execute(request);
        verifyNoInteractions(retryRule, delayStrategy);
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    void shouldRetryWhenRuleAllowsAndReturnSuccessfulResponse() {
        TelegramBotExecutor delegate = mock(TelegramBotExecutor.class);
        CompositeRetryRule retryRule = mock(CompositeRetryRule.class);
        RetryDelayStrategy delayStrategy = mock(RetryDelayStrategy.class);
        RetryingTelegramBotExecutor executor =
                new RetryingTelegramBotExecutor(delegate, retryRule, delayStrategy);
        BaseRequest request = mock(BaseRequest.class);
        TelegramRequestException failure = new TelegramRequestException("temporary");
        BaseResponse response = mock(BaseResponse.class);

        when(delegate.execute(request))
                .thenThrow(failure)
                .thenReturn(response);
        when(retryRule.shouldRetry(request, failure, 1)).thenReturn(true);
        when(delayStrategy.nextDelay(request, failure, 1)).thenReturn(Duration.ZERO);

        BaseResponse actual = executor.execute(request);

        assertSame(response, actual);
        verify(delegate, times(2)).execute(request);
        verify(retryRule).shouldRetry(request, failure, 1);
        verify(delayStrategy).nextDelay(request, failure, 1);
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    void shouldPropagateExceptionWhenRuleRejectsRetry() {
        TelegramBotExecutor delegate = mock(TelegramBotExecutor.class);
        CompositeRetryRule retryRule = mock(CompositeRetryRule.class);
        RetryDelayStrategy delayStrategy = mock(RetryDelayStrategy.class);
        RetryingTelegramBotExecutor executor =
                new RetryingTelegramBotExecutor(delegate, retryRule, delayStrategy);
        BaseRequest request = mock(BaseRequest.class);
        TelegramRequestException failure = new TelegramRequestException("fatal");

        when(delegate.execute(request)).thenThrow(failure);
        when(retryRule.shouldRetry(request, failure, 1)).thenReturn(false);

        TelegramRequestException actual = assertThrows(TelegramRequestException.class,
                () -> executor.execute(request));

        assertSame(failure, actual);
        verify(delegate).execute(request);
        verify(retryRule).shouldRetry(request, failure, 1);
        verifyNoInteractions(delayStrategy);
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    void shouldThrowIllegalStateExceptionAndRestoreInterruptFlagWhenSleepIsInterrupted() {
        TelegramBotExecutor delegate = mock(TelegramBotExecutor.class);
        CompositeRetryRule retryRule = mock(CompositeRetryRule.class);
        RetryDelayStrategy delayStrategy = mock(RetryDelayStrategy.class);
        RetryingTelegramBotExecutor executor =
                new RetryingTelegramBotExecutor(delegate, retryRule, delayStrategy);
        BaseRequest request = mock(BaseRequest.class);
        TelegramRequestException failure = new TelegramRequestException("temporary");

        when(delegate.execute(request)).thenThrow(failure);
        when(retryRule.shouldRetry(request, failure, 1)).thenReturn(true);
        when(delayStrategy.nextDelay(request, failure, 1)).thenReturn(Duration.ofMillis(1));

        Thread.currentThread().interrupt();

        try {
            IllegalStateException actual = assertThrows(IllegalStateException.class,
                    () -> executor.execute(request));

            assertTrue(actual.getMessage().contains("Retry sleep was interrupted"));
            assertSame(InterruptedException.class, actual.getCause().getClass());
            assertTrue(Thread.currentThread().isInterrupted());
            verify(delegate).execute(request);
            verify(retryRule).shouldRetry(request, failure, 1);
            verify(delayStrategy).nextDelay(request, failure, 1);
        } finally {
            Thread.interrupted();
        }
    }
}
