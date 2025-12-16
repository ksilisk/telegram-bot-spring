package io.ksilisk.telegrambot.observability.configuration;

import io.ksilisk.telegrambot.observability.bpp.DeliveryThreadPoolExecutorFactoryObservabilityBeanPostProcessor;
import io.ksilisk.telegrambot.observability.bpp.TelegramBotExecutorObservabilityBeanPostProcessor;
import io.ksilisk.telegrambot.observability.bpp.UpdateDeliveryObservabilityBeanPostProcessor;
import io.ksilisk.telegrambot.observability.bpp.UpdateHandlerObservabilityBeanPostProcessor;
import io.ksilisk.telegrambot.observability.recorder.MetricsRecorder;
import io.ksilisk.telegrambot.observability.resolver.TelegramBotChannelResolver;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class TelegramBotObservabilityBeanPostProcessorConfiguration {
    @Bean
    public static DeliveryThreadPoolExecutorFactoryObservabilityBeanPostProcessor deliveryThreadPoolExecutorFactoryOBPP(
            ObjectProvider<MetricsRecorder> metricsRecorderObjectProvider) {
        return new DeliveryThreadPoolExecutorFactoryObservabilityBeanPostProcessor(metricsRecorderObjectProvider);
    }

    @Bean
    public static UpdateDeliveryObservabilityBeanPostProcessor updateDeliveryObservabilityBeanPostProcessor(
            ObjectProvider<MetricsRecorder> metricsRecorderObjectProvider) {
        return new UpdateDeliveryObservabilityBeanPostProcessor(metricsRecorderObjectProvider);
    }

    @Bean
    public static TelegramBotExecutorObservabilityBeanPostProcessor telegramBotExecutorObservabilityBeanPostProcessor(
            ObjectProvider<MetricsRecorder> metricsRecorderObjectProvider) {
        return new TelegramBotExecutorObservabilityBeanPostProcessor(metricsRecorderObjectProvider);
    }

    @Bean
    public static UpdateHandlerObservabilityBeanPostProcessor updateHandlerObservabilityBeanPostProcessor(
            ObjectProvider<MetricsRecorder> metricsRecorderObjectProvider,
            ObjectProvider<TelegramBotChannelResolver> telegramBotChannelResolver) {
        return new UpdateHandlerObservabilityBeanPostProcessor(metricsRecorderObjectProvider, telegramBotChannelResolver);
    }
}
