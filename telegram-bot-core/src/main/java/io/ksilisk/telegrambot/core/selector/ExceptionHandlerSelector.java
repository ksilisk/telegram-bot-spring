package io.ksilisk.telegrambot.core.selector;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.exception.ExceptionHandler;

import java.util.List;

@FunctionalInterface
public interface ExceptionHandlerSelector {
    List<ExceptionHandler> select(List<ExceptionHandler> exceptionHandlers, Throwable t, Update update);
}
