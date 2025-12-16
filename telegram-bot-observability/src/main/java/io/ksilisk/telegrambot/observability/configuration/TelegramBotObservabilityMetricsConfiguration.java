package io.ksilisk.telegrambot.observability.configuration;

import io.ksilisk.telegrambot.observability.TelegramBotObservabilityAutoConfiguration;
import io.ksilisk.telegrambot.observability.handler.exception.ObservabilityUpdateExceptionHandler;
import io.ksilisk.telegrambot.observability.nomatch.ObservabilityUpdateNoMatchStrategy;
import io.ksilisk.telegrambot.observability.properties.TelegramBotObservabilityProperties;
import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import io.ksilisk.telegrambot.observability.recorder.impl.MicrometerMetricsRecorder;
import io.ksilisk.telegrambot.observability.recorder.impl.NoopMetricsRecorder;
import io.ksilisk.telegrambot.observability.resolver.DefaultTelegramBotChannelResolver;
import io.ksilisk.telegrambot.observability.resolver.TelegramBotChannelResolver;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class TelegramBotObservabilityMetricsConfiguration {
    private static final Logger log = LoggerFactory.getLogger(TelegramBotObservabilityAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(ObservabilityUpdateNoMatchStrategy.class)
    public ObservabilityUpdateNoMatchStrategy observabilityUpdateNoMatchStrategy(MetricsRecorder metricsRecorder) {
        return new ObservabilityUpdateNoMatchStrategy(metricsRecorder);
    }

    @Bean
    @ConditionalOnMissingBean(ObservabilityUpdateExceptionHandler.class)
    public ObservabilityUpdateExceptionHandler observabilityUpdateExceptionHandler(MetricsRecorder metricsRecorder) {
        return new ObservabilityUpdateExceptionHandler(metricsRecorder);
    }

    @Bean
    @ConfigurationProperties(prefix = "telegram.bot.observability")
    public TelegramBotObservabilityProperties telegramBotObservabilityProperties() {
        return new TelegramBotObservabilityProperties();
    }

    @Bean
    @ConditionalOnMissingBean(MetricsRecorder.class)
    @ConditionalOnBean(MeterRegistry.class)
    @ConditionalOnClass(name = "io.micrometer.core.instrument.MeterRegistry")
    public MetricsRecorder micrometerMetricsRecorder(MeterRegistry meterRegistry) {
        return new MicrometerMetricsRecorder(meterRegistry);
    }

    @Bean
    @ConditionalOnMissingBean(MetricsRecorder.class)
    public MetricsRecorder noopMetricsRecorder() {
        log.warn("Telegram Bot Observability is enabled, but no MeterRegistry was found. " +
                "Metrics are disabled (NoopMetricsRecorder).");
        return new NoopMetricsRecorder();
    }

    @Bean
    @ConditionalOnMissingBean(TelegramBotChannelResolver.class)
    public TelegramBotChannelResolver telegramBotChannelResolver() {
        return new DefaultTelegramBotChannelResolver();
    }
}
