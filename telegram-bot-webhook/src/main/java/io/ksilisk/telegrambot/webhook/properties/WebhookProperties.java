package io.ksilisk.telegrambot.webhook.properties;

import io.ksilisk.telegrambot.webhook.controller.WebhookController;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration properties for Telegram webhook mode.
 *
 * <p>Controls the webhook endpoint, registration behavior, security options
 * and the parameters supplied to the Telegram Bot API when registering or
 * removing a webhook.</p>
 */
public class WebhookProperties {
    private static final String DEFAULT_ENDPOINT = "/telegrambot/webhook";
    private static final int DEFAULT_MAX_CONNECTIONS = 10;
    private static final boolean DEFAULT_DROP_PENDING_UPDATES_ON_REGISTER = false;
    private static final boolean DEFAULT_DROP_PENDING_UPDATES_ON_REMOVE = false;
    private static final boolean DEFAULT_AUTO_REGISTER = true;
    private static final boolean DEFAULT_AUTO_REMOVE = true;

    /**
     * Local HTTP endpoint that receives webhook updates.
     *
     * <p>Must match the application route exposed by the {@link WebhookController}.</p>
     */
    @NotBlank
    private String endpoint = DEFAULT_ENDPOINT;

    /**
     * Publicly accessible HTTPS URL that Telegram should call.
     *
     * <p>If not set, the URL must be configured externally in the Bot API or
     * provided through custom lifecycle logic.</p>
     */
    private String externalUrl;

    /**
     * Path to the public certificate file used for self-signed HTTPS endpoints.
     */
    private String certificatePath;

    /**
     * IP address to be sent to Telegram to restrict webhook delivery.
     */
    private String idAddress;

    /**
     * Secret token to validate webhook requests from Telegram.
     */
    private String secretToken;

    /**
     * List of allowed update types for webhook delivery.
     *
     * <p>Empty list means no filtering (Telegram default).</p>
     */
    private List<String> allowedUpdates = new ArrayList<>();

    /**
     * Whether to drop all pending updates when registering a webhook.
     */
    private boolean dropPendingUpdatesOnRegister = DEFAULT_DROP_PENDING_UPDATES_ON_REGISTER;

    /**
     * Whether to drop all pending updates when removing a webhook.
     */
    private boolean dropPendingUpdatesOnRemove = DEFAULT_DROP_PENDING_UPDATES_ON_REMOVE;

    /**
     * Maximum number of simultaneous HTTPS connections Telegram can use
     * to deliver updates.
     */
    @Min(1)
    @Max(100)
    private int maxConnections = DEFAULT_MAX_CONNECTIONS;

    /**
     * Whether to automatically remove the webhook on application shutdown.
     */
    private boolean autoRemove = DEFAULT_AUTO_REMOVE;

    /**
     * Whether to automatically register the webhook on application startup.
     */
    private boolean autoRegister = DEFAULT_AUTO_REGISTER;


    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getCertificatePath() {
        return certificatePath;
    }

    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
    }

    public String getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(String idAddress) {
        this.idAddress = idAddress;
    }

    public List<String> getAllowedUpdates() {
        return allowedUpdates;
    }

    public void setAllowedUpdates(List<String> allowedUpdates) {
        this.allowedUpdates = allowedUpdates;
    }

    public boolean getDropPendingUpdatesOnRegister() {
        return dropPendingUpdatesOnRegister;
    }

    public void setDropPendingUpdatesOnRegister(boolean dropPendingUpdatesOnRegister) {
        this.dropPendingUpdatesOnRegister = dropPendingUpdatesOnRegister;
    }

    public String getSecretToken() {
        return secretToken;
    }

    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public boolean getAutoRemove() {
        return autoRemove;
    }

    public void setAutoRemove(boolean autoRemove) {
        this.autoRemove = autoRemove;
    }

    public boolean getAutoRegister() {
        return autoRegister;
    }

    public void setAutoRegister(boolean autoRegister) {
        this.autoRegister = autoRegister;
    }

    public boolean getDropPendingUpdatesOnRemove() {
        return dropPendingUpdatesOnRemove;
    }

    public void setDropPendingUpdatesOnRemove(boolean dropPendingUpdatesOnRemove) {
        this.dropPendingUpdatesOnRemove = dropPendingUpdatesOnRemove;
    }
}
