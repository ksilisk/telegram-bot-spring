package io.ksilisk.telegrambot.autoconfigure.executor;

import com.google.gson.Gson;
import com.pengrad.telegrambot.model.request.InputFile;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import io.ksilisk.telegrambot.core.exception.request.TelegramRequestException;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class OkHttpTelegramBotExecutor implements TelegramBotExecutor {

    private final OkHttpClient okHttpClient;
    private final Gson gson;
    private final String baseUrl;

    public OkHttpTelegramBotExecutor(OkHttpClient okHttpClient, Gson gson, String baseUrl) {
        this.okHttpClient = okHttpClient;
        this.gson = gson;
        this.baseUrl = baseUrl;
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request)
            throws TelegramRequestException {
        try {
            Request httpRequest = createRequest(request);

            try (Response response = okHttpClient.newCall(httpRequest).execute()) {
                ResponseBody body = response.body();
                if (body == null) {
                    throw new TelegramRequestException("Request Failed. No response body received");
                }

                String responseBody = body.string();
                R result = gson.fromJson(responseBody, request.getResponseType());

                if (result == null) {
                    throw new TelegramRequestException("Request Failed. Could not parse response");
                } else if (!result.isOk()) {
                    String errorMessage = String.format("Request failed. Error code : %d, Reason : %s",
                            result.errorCode(), result.description());
                    throw new TelegramRequestException(errorMessage);
                }

                return result;
            }
        } catch (IOException e) {
            throw new TelegramRequestException("Request failed due to IO error: " + e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof TelegramRequestException) {
                throw (TelegramRequestException) e;
            }
            throw new TelegramRequestException("Request failed: " + e.getMessage(), e);
        }
    }

    private Request createRequest(BaseRequest<?, ?> request) {
        String url = baseUrl + request.getMethod();
        RequestBody requestBody = createRequestBody(request);

        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private RequestBody createRequestBody(BaseRequest<?, ?> request) {
        if (request.isMultipart()) {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            for (Map.Entry<String, Object> parameter : request.getParameters().entrySet()) {
                String name = parameter.getKey();
                Object value = parameter.getValue();

                if (value == null) {
                    continue;
                }

                if (value instanceof byte[]) {
                    MediaType contentType = MediaType.parse(request.getContentType());
                    builder.addFormDataPart(name, request.getFileName(),
                            RequestBody.create((byte[]) value, contentType));
                } else if (value instanceof File) {
                    MediaType contentType = MediaType.parse(request.getContentType());
                    builder.addFormDataPart(name, request.getFileName(),
                            RequestBody.create((File) value, contentType));
                } else if (value instanceof InputFile) {
                    InputFile inputFile = (InputFile) value;
                    MediaType contentType = MediaType.parse(inputFile.getContentType());
                    RequestBody body;

                    if (inputFile.getFile() != null) {
                        body = RequestBody.create(inputFile.getFile(), contentType);
                    } else if (inputFile.getBytes() != null) {
                        body = RequestBody.create(inputFile.getBytes(), contentType);
                    } else {
                        continue;
                    }

                    builder.addFormDataPart(name, inputFile.getFileName(), body);
                } else {
                    builder.addFormDataPart(name, toParamValue(value));
                }
            }

            return builder.build();
        } else {
            FormBody.Builder builder = new FormBody.Builder();

            for (Map.Entry<String, Object> parameter : request.getParameters().entrySet()) {
                if (parameter.getValue() != null) {
                    builder.add(parameter.getKey(), toParamValue(parameter.getValue()));
                }
            }

            return builder.build();
        }
    }

    private String toParamValue(Object obj) {
        if (obj == null) {
            return "";
        }

        Class<?> clazz = obj.getClass();
        if (clazz.isPrimitive() || clazz.isEnum() || clazz.getName().startsWith("java.lang")) {
            return String.valueOf(obj);
        }

        return gson.toJson(obj);
    }
}
