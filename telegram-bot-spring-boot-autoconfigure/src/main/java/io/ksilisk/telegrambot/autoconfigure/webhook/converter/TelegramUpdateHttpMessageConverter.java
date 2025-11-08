package io.ksilisk.telegrambot.autoconfigure.webhook.converter;

import com.google.gson.Gson;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.utility.BotUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.*;
import java.util.List;

public class TelegramUpdateHttpMessageConverter extends AbstractHttpMessageConverter<Update> {
    private final Gson gson = BotUtils.GSON;

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return List.of(MediaType.APPLICATION_JSON);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Update.class.isAssignableFrom(clazz);
    }

    @Override
    protected Update readInternal(Class<? extends Update> clazz,
                                  HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        try (Reader reader = new InputStreamReader(inputMessage.getBody())) {
            return gson.fromJson(reader, Update.class);
        }
    }

    @Override
    protected void writeInternal(Update update,
                                 HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        try (Writer writer = new OutputStreamWriter(outputMessage.getBody())) {
            gson.toJson(update, writer);
        }
    }
}
