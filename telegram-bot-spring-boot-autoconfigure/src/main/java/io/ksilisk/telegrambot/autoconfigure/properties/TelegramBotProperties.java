package io.ksilisk.telegrambot.autoconfigure.properties;

import io.ksilisk.telegrambot.core.properties.DeliveryProperties;
import io.ksilisk.telegrambot.core.properties.ExceptionHandlerProperties;
import io.ksilisk.telegrambot.core.properties.NoMatchStrategyProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "telegram.bot")
public class TelegramBotProperties {
    private static final TelegramBotMode DEFAULT_TELEGRAM_BOT_MODE = TelegramBotMode.LONG_POLLING;

    public enum TelegramBotMode {
        LONG_POLLING,
        WEBHOOK
    }

    private String botUsername;

    @NotNull
    private TelegramBotMode mode = DEFAULT_TELEGRAM_BOT_MODE;

    @Valid
    @NotNull
    @NestedConfigurationProperty
    private NoMatchStrategyProperties nomatch = new NoMatchStrategyProperties();

    @Valid
    @NotNull
    @NestedConfigurationProperty
    private ExceptionHandlerProperties exception = new ExceptionHandlerProperties();

    @Valid
    @NotNull
    @NestedConfigurationProperty
    private HttpClientProperties httpClient = new HttpClientProperties();

    @Valid
    @NotNull
    @NestedConfigurationProperty
    private DeliveryProperties delivery = new DeliveryProperties();

    public DeliveryProperties getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryProperties delivery) {
        this.delivery = delivery;
    }

    public ExceptionHandlerProperties getException() {
        return exception;
    }

    public void setException(ExceptionHandlerProperties exception) {
        this.exception = exception;
    }

    public TelegramBotMode getMode() {
        return mode;
    }

    public void setMode(TelegramBotMode mode) {
        this.mode = mode;
    }

    public String getBotUsername() {
        return botUsername;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    public NoMatchStrategyProperties getNomatch() {
        return nomatch;
    }

    public void setNomatch(NoMatchStrategyProperties nomatch) {
        this.nomatch = nomatch;
    }

    public HttpClientProperties getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClientProperties httpClient) {
        this.httpClient = httpClient;
    }
}
