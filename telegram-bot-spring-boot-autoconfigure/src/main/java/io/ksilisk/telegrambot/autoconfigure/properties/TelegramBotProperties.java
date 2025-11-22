package io.ksilisk.telegrambot.autoconfigure.properties;

import io.ksilisk.telegrambot.core.properties.DeliveryProperties;
import io.ksilisk.telegrambot.core.properties.ExceptionHandlerProperties;
import io.ksilisk.telegrambot.core.properties.NoMatchStrategyProperties;
import io.ksilisk.telegrambot.core.properties.RoutingProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

/**
 * Root configuration properties for Telegram bot integration.
 *
 * <p>Defines the bot token, mode of operation (long polling or webhook),
 * optional test server usage, and nested configuration for delivery,
 * error handling, HTTP client selection and fallback strategies.</p>
 */
@Validated
@ConfigurationProperties(prefix = "telegram.bot")
public class TelegramBotProperties {
    private static final TelegramBotMode DEFAULT_TELEGRAM_BOT_MODE = TelegramBotMode.LONG_POLLING;
    private static final boolean DEFAULT_USE_TEST_SERVER = false;

    /**
     * Bot operation mode.
     *
     * <ul>
     *   <li>{@code LONG_POLLING} – receive updates by polling the Telegram API</li>
     *   <li>{@code WEBHOOK} – receive updates via incoming HTTP requests</li>
     * </ul>
     */
    public enum TelegramBotMode {
        LONG_POLLING,
        WEBHOOK
    }

    /**
     * Bot token obtained from BotFather.
     */
    @NotBlank
    private String token;

    /**
     * Optional bot username.
     * Used for some command matching scenarios and Telegram API operations.
     */
    private String botUsername;

    /**
     * Whether to use the Telegram test server instead of the production API.
     */
    private boolean useTestServer = DEFAULT_USE_TEST_SERVER;

    /**
     * Bot operation mode (long polling or webhook).
     */
    @NotNull
    private TelegramBotMode mode = DEFAULT_TELEGRAM_BOT_MODE;

    /**
     * Configuration for handling updates that do not match any handler.
     */
    @Valid
    @NotNull
    @NestedConfigurationProperty
    private NoMatchStrategyProperties nomatch = new NoMatchStrategyProperties();

    /**
     * Configuration for handling exceptions during update processing.
     */
    @Valid
    @NotNull
    @NestedConfigurationProperty
    private ExceptionHandlerProperties exception = new ExceptionHandlerProperties();

    /**
     * Configuration for the delivery thread pool used to dispatch updates.
     */
    @Valid
    @NotNull
    @NestedConfigurationProperty
    private DeliveryProperties delivery = new DeliveryProperties();

    /**
     * Configuration for selecting and configuring the underlying HTTP client.
     */
    @Valid
    @NotNull
    @NestedConfigurationProperty
    private ClientProperties client = new ClientProperties();

    /**
     * Configuration for routing different types of updates.
     */
    @Valid
    @NotNull
    @NestedConfigurationProperty
    private RoutingProperties routing = new RoutingProperties();

    public RoutingProperties getRouting() {
        return routing;
    }

    public void setRouting(RoutingProperties routing) {
        this.routing = routing;
    }

    public ClientProperties getClient() {
        return client;
    }

    public void setClient(ClientProperties client) {
        this.client = client;
    }

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
