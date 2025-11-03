package io.ksilisk.telegrambot.autoconfigure.properties;

import java.time.Duration;

public class HttpClientProperties {
    private static final Duration DEFAULT_READ_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration DEFAULT_WRITE_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration DEFAULT_CALL_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(5);

    private Duration readTimeout = DEFAULT_READ_TIMEOUT;
    private Duration writeTimeout = DEFAULT_WRITE_TIMEOUT;
    private Duration callTimeout = DEFAULT_CALL_TIMEOUT;
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
