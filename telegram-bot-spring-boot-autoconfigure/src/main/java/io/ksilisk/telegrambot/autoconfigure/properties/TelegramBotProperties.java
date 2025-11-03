package io.ksilisk.telegrambot.autoconfigure.properties;

import io.ksilisk.telegrambot.core.properties.DeliveryProperties;
import io.ksilisk.telegrambot.core.properties.ExceptionHandlerProperties;
import io.ksilisk.telegrambot.core.properties.LongPollingProperties;
import io.ksilisk.telegrambot.core.properties.NoMatchStrategyProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String token;

    private String botUsername;

    @NotNull
    private TelegramBotMode mode = DEFAULT_TELEGRAM_BOT_MODE;

    private boolean useTestServer;

    @Valid
    @NotNull
    @NestedConfigurationProperty
    private LongPollingProperties longPolling = new LongPollingProperties();

    @Valid
    @NotNull
    @NestedConfigurationProperty
    private WebhookProperties webhook = new WebhookProperties();

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBotUsername() {
        return botUsername;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    public boolean getUseTestServer() {
        return useTestServer;
    }

    public void setUseTestServer(boolean useTestServer) {
        this.useTestServer = useTestServer;
    }

    public LongPollingProperties getLongPolling() {
        return longPolling;
    }

    public void setLongPolling(LongPollingProperties longPolling) {
        this.longPolling = longPolling;
    }

    public WebhookProperties getWebhook() {
        return webhook;
    }

    public void setWebhook(WebhookProperties webhook) {
        this.webhook = webhook;
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
