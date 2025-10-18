package io.ksilisk.telegrambot.core.selector.impl;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.exception.ExceptionHandler;
import io.ksilisk.telegrambot.core.selector.ExceptionHandlerSelector;

import java.util.List;

public class DefaultExceptionHandlerSelector implements ExceptionHandlerSelector {
    @Override
    public List<ExceptionHandler> select(List<ExceptionHandler> exceptionHandlers, Throwable t, Update update) {
        return exceptionHandlers;
    }
}
