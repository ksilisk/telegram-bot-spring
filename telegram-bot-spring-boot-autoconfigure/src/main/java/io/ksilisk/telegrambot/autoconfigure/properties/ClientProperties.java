package io.ksilisk.telegrambot.autoconfigure.properties;

import io.ksilisk.telegrambot.core.properties.OkHttpClientProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Configuration properties for selecting the underlying HTTP client
 * used to communicate with the Telegram Bot API.
 *
 * <p>The {@link ClientImplementation} determines whether the starter will use
 * OkHttp, Spring's {@code RestClient}, or select an implementation
 * automatically based on the classpath.</p>
 */
public class ClientProperties {
    private static final ClientImplementation DEFAULT_IMPLEMENTATION = ClientImplementation.AUTO;

    /**
     * Client implementation selection strategy.
     *
     * <ul>
     *   <li>{@code AUTO} – prefer OkHttp if present, otherwise fall back to Spring client</li>
     *   <li>{@code OKHTTP} – require OkHttp, fail if not available</li>
     *   <li>{@code SPRING} – require Spring client, fail if not available</li>
     * </ul>
     */
    public enum ClientImplementation {
        AUTO,
        OKHTTP,
        SPRING
    }

    /**
     * HTTP client configuration used when the OkHttp-based client
     * implementation is selected.
     *
     * <p>Ignored for Spring-based client implementations.</p>
     */
    @Valid
    @NotNull
    @NestedConfigurationProperty
    private OkHttpClientProperties okhttp = new OkHttpClientProperties();

    private ClientImplementation implementation = DEFAULT_IMPLEMENTATION;

    public ClientImplementation getImplementation() {
        return implementation;
    }

    public void setImplementation(ClientImplementation implementation) { this.implementation = implementation;}

    public OkHttpClientProperties getOkhttp() {
        return okhttp;
    }

    public void setOkhttp(OkHttpClientProperties okhttp) {
        this.okhttp = okhttp;
    }

}
