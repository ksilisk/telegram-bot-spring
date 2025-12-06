package io.ksilisk.telegrambot.webhook;

import io.ksilisk.telegrambot.core.delivery.UpdateDelivery;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.webhook.controller.DefaultWebhookController;
import io.ksilisk.telegrambot.webhook.controller.WebhookController;
import io.ksilisk.telegrambot.webhook.converter.TelegramUpdateHttpMessageConverter;
import io.ksilisk.telegrambot.webhook.filter.WebhookSecretTokenFilter;
import io.ksilisk.telegrambot.webhook.ingress.DefaultWebhookUpdateIngress;
import io.ksilisk.telegrambot.webhook.ingress.WebhookUpdateIngress;
import io.ksilisk.telegrambot.webhook.lifecycle.DefaultWebhookLifecycle;
import io.ksilisk.telegrambot.webhook.lifecycle.WebhookLifecycle;
import io.ksilisk.telegrambot.webhook.properties.WebhookProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

@AutoConfiguration
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "WEBHOOK")
public class TelegramBotWebhookAutoConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "telegram.bot.webhook")
    public WebhookProperties webhookProperties() {
        return new WebhookProperties();
    }

    @Bean
    @ConditionalOnMissingBean(WebhookUpdateIngress.class)
    public WebhookUpdateIngress webhookUpdateIngress(UpdateDelivery updateDelivery) {
        return new DefaultWebhookUpdateIngress(updateDelivery);
    }

    @Bean
    @ConditionalOnMissingBean(WebhookController.class)
    public DefaultWebhookController webhookController(WebhookUpdateIngress webhookUpdateIngress) {
        return new DefaultWebhookController(webhookUpdateIngress);
    }

    @Bean
    public FilterRegistrationBean<WebhookSecretTokenFilter> webhookSecretTokenFilter(
            WebhookProperties webhookProperties) {
        WebhookSecretTokenFilter webhookSecretTokenFilter = new WebhookSecretTokenFilter(
                webhookProperties.getSecretToken(), webhookProperties.getEndpoint());

        FilterRegistrationBean<WebhookSecretTokenFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(webhookSecretTokenFilter);
        filterRegistrationBean.addUrlPatterns(webhookProperties.getEndpoint());
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
    }

    @Bean
    @ConditionalOnMissingBean(WebhookLifecycle.class)
    public WebhookLifecycle webhookLifecycle(WebhookProperties webhookProperties,
                                             TelegramBotExecutor telegramBotExecutor) {
        return new DefaultWebhookLifecycle(webhookProperties, telegramBotExecutor);
    }

    @Bean
    @ConditionalOnMissingBean(TelegramUpdateHttpMessageConverter.class)
    public TelegramUpdateHttpMessageConverter telegramUpdateHttpMessageConverter() {
        return new TelegramUpdateHttpMessageConverter();
    }
}
