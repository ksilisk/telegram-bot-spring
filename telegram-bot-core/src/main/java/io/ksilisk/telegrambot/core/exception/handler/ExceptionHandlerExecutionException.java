package io.ksilisk.telegrambot.core.exception.handler;

public class ExceptionHandlerExecutionException extends RuntimeException {
    public ExceptionHandlerExecutionException(String message) {
        super(message);
    }

  public ExceptionHandlerExecutionException(Throwable cause) {
    super(cause);
  }
}
