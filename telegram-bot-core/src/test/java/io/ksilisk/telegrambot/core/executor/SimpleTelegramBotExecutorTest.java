package io.ksilisk.telegrambot.core.executor;

import com.pengrad.telegrambot.impl.TelegramBotClient;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import io.ksilisk.telegrambot.core.exception.request.RequestFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SimpleTelegramBotExecutorTest {

    private TelegramBotClient mockTelegramBot;
    private SimpleTelegramBotExecutor executor;
    private SendMessage mockRequest;
    private SendResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockTelegramBot = mock(TelegramBotClient.class);
        executor = new SimpleTelegramBotExecutor(mockTelegramBot);

        mockRequest = mock(SendMessage.class);
        mockResponse = mock(SendResponse.class);
    }

    @Test
    void testExecute_Success() {
        when(mockResponse.isOk()).thenReturn(true);
        when(mockTelegramBot.send(any())).thenReturn(mockResponse);

        SendResponse result = executor.execute(mockRequest);

        assertEquals(mockResponse, result);
        verify(mockTelegramBot, times(1)).send(mockRequest);
    }

    @Test
    void testExecute_FailedResponse() {
        when(mockResponse.isOk()).thenReturn(false);
        when(mockResponse.errorCode()).thenReturn(400);
        when(mockResponse.description()).thenReturn("Bad Request: chat not found");
        when(mockTelegramBot.send(any())).thenReturn(mockResponse);

        RequestFailedException thrown = assertThrows(RequestFailedException.class, () -> executor.execute(mockRequest));

        assertTrue(thrown.getMessage().contains("Error code : 400"));
        assertTrue(thrown.getMessage().contains("Reason : Bad Request: chat not found"));
        verify(mockTelegramBot, times(1)).send(mockRequest);
    }
    @Test
    void testExecute_NullResponse() {
        when(mockTelegramBot.send(any())).thenReturn(null);

        RequestFailedException thrown = assertThrows(RequestFailedException.class, () -> {
            executor.execute(mockRequest);
        });

        assertEquals("Request Failed. No response received", thrown.getMessage());
        verify(mockTelegramBot, times(1)).send(mockRequest);
    }
}