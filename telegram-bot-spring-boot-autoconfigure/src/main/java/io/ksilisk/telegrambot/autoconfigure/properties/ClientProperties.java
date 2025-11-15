package io.ksilisk.telegrambot.autoconfigure.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

public class ClientProperties {
    private static final ClientImplementation DEFAULT_IMPLEMENTATION = ClientImplementation.AUTO;

    public enum ClientImplementation {
        AUTO,
        OKHTTP,
        SPRING
    }

    @Valid
    @NotNull
    @NestedConfigurationProperty
    private HttpClientProperties httpClient = new HttpClientProperties();

    private ClientImplementation implementation = DEFAULT_IMPLEMENTATION;

    public ClientImplementation getImplementation() {
        return implementation;
    }

    public void setImplementation(ClientImplementation implementation) { this.implementation = implementation;}

    public HttpClientProperties getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClientProperties httpClient) {
        this.httpClient = httpClient;
    }

}
