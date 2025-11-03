package io.ksilisk.telegrambot.core.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.Duration;

public class DeliveryProperties {
    private static final int DEFAULT_MAX_THREADS = 1;
    private static final int DEFAULT_MIN_THREADS = 1;
    private static final int DEFAULT_QUEUE_CAPACITY = Integer.MAX_VALUE;
    private static final Duration DEFAULT_THREAD_ALIVE_TIME = Duration.ofSeconds(60);
    private static final Duration DEFAULT_SHUTDOWN_TIMEOUT = Duration.ofSeconds(2);

    @Min(1)
    private int maxThreads = DEFAULT_MAX_THREADS;

    @Min(1)
    private int minThreads = DEFAULT_MIN_THREADS;

    @Min(1)
    @Max(Integer.MAX_VALUE)
    private int queueCapacity = DEFAULT_QUEUE_CAPACITY;
    private Duration threadAliveTime = DEFAULT_THREAD_ALIVE_TIME;
    private Duration shutdownTimeout = DEFAULT_SHUTDOWN_TIMEOUT;

    public Duration getShutdownTimeout() {
        return shutdownTimeout;
    }

    public void setShutdownTimeout(Duration shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }

    public Duration getThreadAliveTime() {
        return threadAliveTime;
    }

    public void setThreadAliveTime(Duration threadAliveTime) {
        this.threadAliveTime = threadAliveTime;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public int getMinThreads() {
        return minThreads;
    }

    public void setMinThreads(int minThreads) {
        this.minThreads = minThreads;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }
}
