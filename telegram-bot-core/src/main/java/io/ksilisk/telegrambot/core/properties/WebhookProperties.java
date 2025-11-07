package io.ksilisk.telegrambot.core.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class WebhookProperties {
    private static final String DEFAULT_ENDPOINT = "/telegrambot/webhook";
    private static final int DEFAULT_MAX_CONNECTIONS = 10;
    private static final boolean DEFAULT_DROP_PENDING_UPDATES_ON_REGISTER = false;
    private static final boolean DEFAULT_DROP_PENDING_UPDATES_ON_REMOVE = false;
    private static final boolean DEFAULT_AUTO_REGISTER = true;
    private static final boolean DEFAULT_AUTO_REMOVE = true;

    @NotBlank
    private String endpoint = DEFAULT_ENDPOINT;

    @NotBlank
    private String externalUrl;
    private String certificatePath;
    private String idAddress;
    private String secretToken;
    private List<String> allowedUpdates = new ArrayList<>();
    private boolean dropPendingUpdatesOnRegister = DEFAULT_DROP_PENDING_UPDATES_ON_REGISTER;
    private boolean dropPendingUpdatesOnRemove = DEFAULT_DROP_PENDING_UPDATES_ON_REMOVE;

    @Min(1)
    @Max(100)
    private int maxConnections = DEFAULT_MAX_CONNECTIONS;
    private boolean autoRemove = DEFAULT_AUTO_REMOVE;
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
