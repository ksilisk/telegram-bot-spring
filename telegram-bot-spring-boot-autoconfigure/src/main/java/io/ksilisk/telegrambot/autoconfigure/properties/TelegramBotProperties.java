package io.ksilisk.telegrambot.autoconfigure.properties;

import io.ksilisk.telegrambot.core.properties.DeliveryProperties;
import io.ksilisk.telegrambot.core.properties.ExceptionHandlerProperties;
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
    private static final boolean DEFAULT_USE_TEST_SERVER = false;

    public enum TelegramBotMode {
        LONG_POLLING,
        WEBHOOK
    }

    @NotBlank
    private String token;

    private String botUsername;
    private boolean useTestServer = DEFAULT_USE_TEST_SERVER;

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
    private DeliveryProperties delivery = new DeliveryProperties();

    @Valid
    @NotNull
    @NestedConfigurationProperty
    private ClientProperties client = new ClientProperties();

    public ClientProperties getClient() { return client; }

    public void setClient(ClientProperties client) { this.client = client;}

    public boolean getUseTestServer() {
        return useTestServer;
    }

    public void setUseTestServer(boolean useTestServer) {
        this.useTestServer = useTestServer;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

}
