package io.ksilisk.telegrambot.autoconfigure.executor;

import com.pengrad.telegrambot.impl.TelegramBotClient;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class RestClientTelegramBotExecutorTest {

    private RestClientTelegramBotExecutor executor;
    private TelegramBotClient mockTelegramBot;
    private SendResponse mockResponse;
    private SendMessage mockRequest;

    private static final String BASE_URL = "https://api.telegram.url/bot123/";

    @BeforeEach
    void setUp() {
        mockTelegramBot = mock(TelegramBotClient.class);
        RestClient restClient = mock(RestClient.class);
        executor = new RestClientTelegramBotExecutor(restClient, BASE_URL);

        mockRequest = mock(SendMessage.class);
        mockResponse = mock(SendResponse.class);
    }

    @Test
    void testExecute_Success() {
        when(mockResponse.isOk()).thenReturn(true);
        when(mockTelegramBot.send(any())).thenReturn(mockResponse);

        SendResponse result = executor.execute(mockRequest);
        assertEquals(mockResponse, result);
    }

}