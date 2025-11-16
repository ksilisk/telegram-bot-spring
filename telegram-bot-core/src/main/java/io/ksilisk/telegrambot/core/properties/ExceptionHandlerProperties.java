package io.ksilisk.telegrambot.core.properties;

import io.ksilisk.telegrambot.core.handler.exception.ExceptionHandlerErrorPolicy;

/**
 * Configuration properties for exception handling during update processing.
 *
 * <p>Controls how the framework reacts when an {@code ExceptionHandler}
 * itself throws an error.</p>
 */
public class ExceptionHandlerProperties {
    private static final ExceptionHandlerErrorPolicy DEFAULT_EXCEPTION_HANDLER_ERROR_POLICY = ExceptionHandlerErrorPolicy.LOG;

    /**
     * Error handling policy applied when an {@code ExceptionHandler}
     * throws an exception.
     *
     * <p>Defaults to {@code LOG}.</p>
     */
    private ExceptionHandlerErrorPolicy errorPolicy = DEFAULT_EXCEPTION_HANDLER_ERROR_POLICY;

    public ExceptionHandlerErrorPolicy getErrorPolicy() {
        return errorPolicy;
    }

    public void setErrorPolicy(ExceptionHandlerErrorPolicy errorPolicy) {
        this.errorPolicy = errorPolicy;
    }
}
