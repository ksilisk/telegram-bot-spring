package io.ksilisk.telegrambot.autoconfigure.config.transport.client;

import io.ksilisk.telegrambot.autoconfigure.adapter.SpringCoreOrderAdapter;
import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.retry.RetryDelayStrategy;
import io.ksilisk.telegrambot.core.executor.retry.RetryRule;
import io.ksilisk.telegrambot.core.executor.retry.RetryingTelegramBotExecutor;
import io.ksilisk.telegrambot.core.executor.retry.impl.CompositeRetryRule;
import io.ksilisk.telegrambot.core.executor.retry.impl.FixedRetryDelayStrategy;
import io.ksilisk.telegrambot.core.executor.retry.impl.MaxAttemptsRetryRule;
import io.ksilisk.telegrambot.core.executor.retry.impl.RetryableMethodsRetryRule;
import io.ksilisk.telegrambot.core.order.CoreOrdered;
import io.ksilisk.telegrambot.core.properties.ClientRetryProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "telegram.bot.client.retry.enabled", havingValue = "true")
public class TelegramClientRetryConfiguration {
    private static final Logger log = LoggerFactory.getLogger(TelegramClientRetryConfiguration.class);

    private final ClientRetryProperties properties;

    public TelegramClientRetryConfiguration(TelegramBotProperties properties) {
        this.properties = properties.getClient().getRetry();
    }

    @Bean
    @ConditionalOnMissingBean
    public RetryDelayStrategy retryDelayStrategy() {
        return new FixedRetryDelayStrategy(properties.getDelay());
    }

    @Bean
    @ConditionalOnMissingBean(name = "maxAttemptsRetryRule")
    public RetryRule maxAttemptsRetryRule() {
        return new MaxAttemptsRetryRule(properties.getMaxAttempts());
    }

    @Bean
    @ConditionalOnMissingBean(name = "retryableMethodsRetryRule")
    public RetryRule retryableMethodsRetryRule() {
        if (properties.getRetryableMethods().isEmpty()) {
            log.warn("Telegram client retry is enabled, but no retryable methods are configured. " +
                    "Retry will never be applied.");
        }
        return new RetryableMethodsRetryRule(properties.getRetryableMethods());
    }

    @Bean
    @ConditionalOnMissingBean(CompositeRetryRule.class)
    public CompositeRetryRule compositeRetryRule(List<RetryRule> retryRuleList) {
        List<RetryRule> adopt = retryRuleList.stream()
                .map(SpringCoreOrderAdapter::adaptIfNecessary)
                .sorted(CoreOrdered.COMPARATOR)
                .toList();
        return new CompositeRetryRule(adopt);
    }

    public static TelegramBotExecutor buildRetryingTelegramBotExecutor(TelegramBotExecutor delegate,
                                                                       CompositeRetryRule compositeRetryRule,
                                                                       RetryDelayStrategy retryDelayStrategy) {
        if (compositeRetryRule == null) {
            throw new IllegalStateException(
                    "Failed to create RetryingTelegramBotExecutor: CompositeRetryRule must not be null.");
        }

        if (retryDelayStrategy == null) {
            throw new IllegalStateException(
                    "Failed to create RetryingTelegramBotExecutor: RetryDelayStrategy must not be null.");
        }

        return new RetryingTelegramBotExecutor(delegate, compositeRetryRule, retryDelayStrategy);
    }
}
