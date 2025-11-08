package io.ksilisk.telegrambot.core.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class LongPollingProperties {
    private static final Duration DEFAULT_RETRY_DELAY = Duration.ofSeconds(2);
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(1);
    private static final Duration DEFAULT_SHUTDOWN_TIMEOUT = Duration.ofSeconds(2);
    private static final boolean DEFAULT_DROP_PENDING_ON_START = false;
    private static final int DEFAULT_LIMIT = 100;

    private Duration retryDelay = DEFAULT_RETRY_DELAY;
    private boolean dropPendingOnStart = DEFAULT_DROP_PENDING_ON_START;
    private List<String> allowedUpdates = new ArrayList<>();

    @Min(1)
    @Max(100)
    private int limit = DEFAULT_LIMIT;

    private Duration timeout = DEFAULT_TIMEOUT;
    private Duration shutdownTimeout = DEFAULT_SHUTDOWN_TIMEOUT;


    public Duration getShutdownTimeout() {
        return shutdownTimeout;
    }

    public void setShutdownTimeout(Duration shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }

    public Duration getRetryDelay() {
        return retryDelay;
    }

    public void setRetryDelay(Duration retryDelay) {
        this.retryDelay = retryDelay;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<String> getAllowedUpdates() {
        return allowedUpdates;
    }

    public void setAllowedUpdates(List<String> allowedUpdates) {
        this.allowedUpdates = allowedUpdates;
    }

    public boolean getDropPendingOnStart() {
        return dropPendingOnStart;
    }

    public void setDropPendingOnStart(boolean dropPendingOnStart) {
        this.dropPendingOnStart = dropPendingOnStart;
    }
}
