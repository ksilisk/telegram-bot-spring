package io.ksilisk.telegrambot.core.executor.retry.impl;

import com.pengrad.telegrambot.request.BaseRequest;
import io.ksilisk.telegrambot.core.executor.retry.RetryRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CompositeRetryRule implements RetryRule {
    private static final Logger log = LoggerFactory.getLogger(CompositeRetryRule.class);

    private final List<RetryRule> rules;

    public CompositeRetryRule(List<RetryRule> rules) {
        this.rules = List.copyOf(rules);
    }

    @Override
    public boolean shouldRetry(BaseRequest<?, ?> request, Throwable error, int attempt) {
        for (RetryRule rule : rules) {
            if (!rule.shouldRetry(request, error, attempt)) {
                log.debug("Retry rejected by [{}] for Telegram method [{}], attempt [{}]",
                        rule.getClass().getSimpleName(),
                        request.getMethod(),
                        attempt);
                return false;
            }
        }
        return true;
    }
}
