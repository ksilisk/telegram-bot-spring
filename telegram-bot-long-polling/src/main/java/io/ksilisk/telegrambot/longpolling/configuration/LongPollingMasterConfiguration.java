package io.ksilisk.telegrambot.longpolling.configuration;

import io.ksilisk.telegrambot.core.executor.resolver.SwitchableTelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.longpolling.failover.MasterFailureClassifier;
import io.ksilisk.telegrambot.longpolling.failover.MasterManager;
import io.ksilisk.telegrambot.longpolling.failover.MasterSwitchPolicy;
import io.ksilisk.telegrambot.longpolling.failover.impl.DefaultMasterFailureClassifier;
import io.ksilisk.telegrambot.longpolling.failover.impl.DefaultMasterManager;
import io.ksilisk.telegrambot.longpolling.failover.policy.duration.DurationBasedMasterSwitchPolicy;
import io.ksilisk.telegrambot.longpolling.properties.LongPollingProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class LongPollingMasterConfiguration {
    @Bean
    @ConditionalOnMissingBean
    MasterFailureClassifier longPollingMasterFailureClassifier() {
        return new DefaultMasterFailureClassifier();
    }

    @Bean
    @ConditionalOnMissingBean
    MasterSwitchPolicy longPollingMasterSwitchPolicy(LongPollingProperties properties,
                                                     MasterFailureClassifier failureClassifier) {
        return switch (properties.getFailover().getPolicy()) {
            case DURATION -> new DurationBasedMasterSwitchPolicy(
                    failureClassifier,
                    properties.getFailover().getDuration().getSwitchAfter()
            );
        };
    }

    @Bean
    @ConditionalOnMissingBean
    MasterManager longPollingMasterManager(MasterSwitchPolicy switchPolicy,
                                           SwitchableTelegramBotApiUrlProvider apiUrlProvider) {
        return new DefaultMasterManager(switchPolicy, apiUrlProvider);
    }
}
