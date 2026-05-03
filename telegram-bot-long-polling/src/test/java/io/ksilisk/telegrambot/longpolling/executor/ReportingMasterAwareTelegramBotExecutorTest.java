package io.ksilisk.telegrambot.longpolling.executor;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.SendResponse;
import io.ksilisk.telegrambot.core.exception.request.TelegramRequestException;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.longpolling.failover.MasterManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


class ReportingMasterAwareTelegramBotExecutorTest {
    @Test
    void shouldRecordSuccessAndReturnResponse() throws TelegramRequestException {
        TelegramBotExecutor delegate = mock(TelegramBotExecutor.class);
        MasterManager masterManager = mock(MasterManager.class);
        ReportingMasterAwareTelegramBotExecutor executor =
                new ReportingMasterAwareTelegramBotExecutor(delegate, masterManager);
        TestRequest request = new TestRequest(null);
        SendResponse response = Mockito.mock(SendResponse.class);
        when(delegate.execute(request)).thenReturn(response);
        SendResponse result = executor.execute(request);
        assertThat(result).isSameAs(response);
        verify(masterManager).recordSuccess();
        verify(masterManager, never()).recordFailure(Mockito.any());
    }

    @Test
    void shouldRecordFailureAndRethrowRuntimeException() throws TelegramRequestException {
        TelegramBotExecutor delegate = mock(TelegramBotExecutor.class);
        MasterManager masterManager = mock(MasterManager.class);
        ReportingMasterAwareTelegramBotExecutor executor =
                new ReportingMasterAwareTelegramBotExecutor(delegate, masterManager);
        TestRequest request = new TestRequest(null);
        RuntimeException exception = new RuntimeException("boom");
        when(delegate.execute(request)).thenThrow(exception);
        assertThatThrownBy(() -> executor.execute(request))
                .isSameAs(exception);
        verify(masterManager, never()).recordSuccess();
        verify(masterManager).recordFailure(same(exception));
    }

    @Test
    void shouldRecordFailureWhenTelegramRequestExceptionIsThrown() throws TelegramRequestException {
        TelegramBotExecutor delegate = mock(TelegramBotExecutor.class);
        MasterManager masterManager = mock(MasterManager.class);
        ReportingMasterAwareTelegramBotExecutor executor =
                new ReportingMasterAwareTelegramBotExecutor(delegate, masterManager);
        TestRequest request = new TestRequest(null);
        TelegramRequestException exception = mock(TelegramRequestException.class);
        when(delegate.execute(request)).thenThrow(exception);
        assertThatThrownBy(() -> executor.execute(request))
                .isSameAs(exception);
        verify(masterManager, never()).recordSuccess();
        verify(masterManager).recordFailure(Mockito.any());
    }

    private static final class TestRequest extends BaseRequest<TestRequest, SendResponse> {

        public TestRequest(Class<? extends SendResponse> responseClass) {
            super(responseClass);
        }

        @Override
        public String getMethod() {
            return "test";
        }
    }
}
