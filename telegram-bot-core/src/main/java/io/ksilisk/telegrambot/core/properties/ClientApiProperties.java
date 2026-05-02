package io.ksilisk.telegrambot.core.properties;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Telegram Bot API client endpoint settings.
 */
public class ClientApiProperties {
    /**
     * Base Telegram Bot API endpoints.
     *
     * <p>Values should not include {@code /bot} or the bot token. Example:
     * {@code https://api.telegram.org}.</p>
     */
    @NotNull
    private List<String> endpoints = new ArrayList<>();

    public List<String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<String> endpoints) {
        this.endpoints = endpoints;
    }
}
