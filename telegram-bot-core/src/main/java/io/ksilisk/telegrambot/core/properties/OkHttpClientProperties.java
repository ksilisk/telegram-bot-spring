package io.ksilisk.telegrambot.core.properties;

import java.time.Duration;

/**
 * Configuration properties for the OkHttp-based Telegram API client.
 *
 * <p>Controls connection, read, write and call timeouts applied to
 * outbound HTTP requests executed through OkHttp.</p>
 */
public class OkHttpClientProperties {
    private static final Duration DEFAULT_READ_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration DEFAULT_WRITE_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration DEFAULT_CALL_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(5);

    /**
     * Maximum time to wait for reading the response body.
     */
    private Duration readTimeout = DEFAULT_READ_TIMEOUT;

    /**
     * Maximum time to wait for writing the request body.
     */
    private Duration writeTimeout = DEFAULT_WRITE_TIMEOUT;

    /**
     * Maximum total time allowed for the entire HTTP call
     * (connection + request + response).
     */
    private Duration callTimeout = DEFAULT_CALL_TIMEOUT;

    /**
     * Maximum time to wait when establishing a new connection.
     */
    private Duration connectTimeout = DEFAULT_CONNECT_TIMEOUT;

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Duration getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(Duration writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public Duration getCallTimeout() {
        return callTimeout;
    }

    public void setCallTimeout(Duration callTimeout) {
        this.callTimeout = callTimeout;
    }

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
}
