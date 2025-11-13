package io.ksilisk.telegrambot.core.executor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.pengrad.telegrambot.model.request.InputFile;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import io.ksilisk.telegrambot.core.exception.request.RequestFailedException;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class RestClientTelegramBotExecutor implements TelegramBotExecutor {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public RestClientTelegramBotExecutor(RestClient restClient,String baseUrl) {
        this.restClient = restClient;
        this.objectMapper = createDefaultObjectMapper();
        this.baseUrl = baseUrl;
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request) {
        try {
            R response;

            if (request.isMultipart()) {
                response = executeMultipartRequest(request);
            } else {
                response = executeFormRequest(request);
            }

            if (response == null) {
                throw new RequestFailedException("Request Failed. No response received");
            } else if (!response.isOk()) {
                String errorMessage = String.format("Request failed. Error code : %d, Reason : %s",
                        response.errorCode(), response.description());
                throw new RequestFailedException(errorMessage);
            }

            return response;
        } catch (Exception e) {
            if (e instanceof RequestFailedException) {
                throw (RequestFailedException) e;
            }
            throw new RequestFailedException("Request failed: " + e.getMessage());
        }
    }


    private static ObjectMapper createDefaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Configuration pour matcher le format snake_case de Telegram
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    private <T extends BaseRequest<T, R>, R extends BaseResponse> R executeMultipartRequest(BaseRequest<T, R> request) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        for (Map.Entry<String, Object> parameter : request.getParameters().entrySet()) {
            String name = parameter.getKey();
            Object value = parameter.getValue();

            if (value instanceof byte[]) {
                builder.part(name, value)
                        .filename(request.getFileName())
                        .contentType(MediaType.parseMediaType(request.getContentType()));
            } else if (value instanceof File) {
                File file = (File) value;
                try {
                    byte[] fileBytes = Files.readAllBytes(file.toPath());
                    builder.part(name, fileBytes)
                            .filename(request.getFileName())
                            .contentType(MediaType.parseMediaType(request.getContentType()));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read file: " + file.getName(), e);
                }
            } else if (value instanceof InputFile) {
                InputFile inputFile = (InputFile) value;
                byte[] bytes = inputFile.getBytes();
                if (bytes == null && inputFile.getFile() != null) {
                    try {
                        bytes = Files.readAllBytes(inputFile.getFile().toPath());
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to read input file", e);
                    }
                }
                builder.part(name, bytes)
                        .filename(inputFile.getFileName())
                        .contentType(MediaType.parseMediaType(inputFile.getContentType()));
            } else {
                builder.part(name, toParamValue(value));
            }
        }

        return restClient.post()
                .uri(baseUrl + request.getMethod())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(builder.build())
                .retrieve()
                .body(request.getResponseType());
    }

    private <T extends BaseRequest<T, R>, R extends BaseResponse> R executeFormRequest(BaseRequest<T, R> request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        for (Map.Entry<String, Object> parameter : request.getParameters().entrySet()) {
            formData.add(parameter.getKey(), toParamValue(parameter.getValue()));
        }

        return restClient.post()
                .uri(baseUrl + request.getMethod())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(request.getResponseType());
    }

    private String toParamValue(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj.getClass().isPrimitive() ||
                obj.getClass().isEnum() ||
                obj.getClass().getName().startsWith("java.lang")) {
            return String.valueOf(obj);
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize parameter: " + obj, e);
        }
    }
}
