package io.ksilisk.telegrambot.core.selector.impl;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.exception.UpdateExceptionHandler;
import io.ksilisk.telegrambot.core.selector.UpdateExceptionHandlerSelector;

import java.util.List;

public class DefaultExceptionHandlerSelector implements UpdateExceptionHandlerSelector {
    @Override
    public List<UpdateExceptionHandler> select(List<UpdateExceptionHandler> updateExceptionHandlers, Throwable t, Update update) {
        return updateExceptionHandlers;
    }
}
