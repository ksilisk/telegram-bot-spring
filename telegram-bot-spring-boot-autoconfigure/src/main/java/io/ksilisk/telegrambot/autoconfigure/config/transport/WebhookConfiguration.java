package io.ksilisk.telegrambot.autoconfigure.config.transport;

import io.ksilisk.telegrambot.autoconfigure.webhook.controller.DefaultWebhookController;
import io.ksilisk.telegrambot.autoconfigure.webhook.converter.TelegramUpdateHttpMessageConverter;
import io.ksilisk.telegrambot.autoconfigure.webhook.filter.WebhookSecretTokenFilter;
import io.ksilisk.telegrambot.autoconfigure.webhook.lifecycle.DefaultWebhookLifecycle;
import io.ksilisk.telegrambot.core.delivery.UpdateDelivery;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.properties.WebhookProperties;
import io.ksilisk.telegrambot.core.webhook.WebhookController;
import io.ksilisk.telegrambot.core.webhook.WebhookLifecycle;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "telegram.bot", name = "mode", havingValue = "WEBHOOK")
@ConditionalOnClass(DispatcherServlet.class)
public class WebhookConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "telegram.bot.webhook")
    public WebhookProperties webhookProperties() {
        return new WebhookProperties();
    }

    @Bean
    @ConditionalOnMissingBean(WebhookController.class)
    public DefaultWebhookController webhookController(UpdateDelivery updateDelivery) {
        return new DefaultWebhookController(updateDelivery);
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
