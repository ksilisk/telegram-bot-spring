package io.ksilisk.telegrambot.core.exception.strategy;

public class StrategyExecutionException extends RuntimeException {
    public StrategyExecutionException(String message) {
        super(message);
    }

    public StrategyExecutionException(Throwable cause) {
        super(cause);
    }
}
