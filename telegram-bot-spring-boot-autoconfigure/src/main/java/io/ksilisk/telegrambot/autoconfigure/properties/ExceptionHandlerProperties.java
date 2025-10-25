package io.ksilisk.telegrambot.autoconfigure.properties;

import io.ksilisk.telegrambot.core.handler.exception.ExceptionHandlerErrorPolicy;

public class ExceptionHandlerProperties {
    private static final ExceptionHandlerErrorPolicy DEFAULT_EXCEPTION_HANDLER_ERROR_POLICY = ExceptionHandlerErrorPolicy.LOG;

    private ExceptionHandlerErrorPolicy errorPolicy = DEFAULT_EXCEPTION_HANDLER_ERROR_POLICY;

    public ExceptionHandlerErrorPolicy getErrorPolicy() {
        return errorPolicy;
    }

    public void setErrorPolicy(ExceptionHandlerErrorPolicy errorPolicy) {
        this.errorPolicy = errorPolicy;
    }
}
