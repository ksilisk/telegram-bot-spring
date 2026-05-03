package io.ksilisk.telegrambot.core.executor.retry;

import io.ksilisk.telegrambot.core.executor.TelegramBotClientFactory;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.retry.impl.CompositeRetryRule;
import io.ksilisk.telegrambot.core.file.TelegramBotFileClient;

public class RetryingTelegramBotClientFactory implements TelegramBotClientFactory {
    private final TelegramBotClientFactory delegate;
    private final boolean retryEnabled;
    private final CompositeRetryRule retryRule;
    private final RetryDelayStrategy retryDelayStrategy;

    public RetryingTelegramBotClientFactory(TelegramBotClientFactory delegate,
                                            boolean retryEnabled,
                                            CompositeRetryRule retryRule,
                                            RetryDelayStrategy retryDelayStrategy) {
        this.delegate = delegate;
        this.retryEnabled = retryEnabled;
        this.retryRule = retryRule;
        this.retryDelayStrategy = retryDelayStrategy;
    }

    @Override
    public TelegramBotExecutor createExecutor() {
        TelegramBotExecutor executor = delegate.createExecutor();
        if (!retryEnabled) {
            return executor;
        }
        if (retryRule == null || retryDelayStrategy == null) {
            return executor;
        }
        return new RetryingTelegramBotExecutor(executor, retryRule, retryDelayStrategy);

    }

    @Override
    public TelegramBotFileClient createFileClient(TelegramBotExecutor telegramBotExecutor) {
        return delegate.createFileClient(telegramBotExecutor);
    }
}
