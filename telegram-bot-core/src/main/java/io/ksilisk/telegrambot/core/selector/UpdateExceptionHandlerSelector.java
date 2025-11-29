package io.ksilisk.telegrambot.core.selector;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.exception.UpdateExceptionHandler;

/**
 * see {@link ExceptionHandlerSelector}
 */
public interface UpdateExceptionHandlerSelector extends ExceptionHandlerSelector<UpdateExceptionHandler, Update> {
}
