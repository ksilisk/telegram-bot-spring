package io.ksilisk.telegrambot.core.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.Duration;

/**
 * Configuration properties for the update delivery thread pool.
 *
 * <p>These properties control the executor used to dispatch updates to
 * handlers, including thread counts, queue size and shutdown behavior.</p>
 */
public class DeliveryProperties {
    private static final int DEFAULT_MAX_THREADS = 1;
    private static final int DEFAULT_MIN_THREADS = 1;
    private static final int DEFAULT_QUEUE_CAPACITY = Integer.MAX_VALUE;
    private static final Duration DEFAULT_THREAD_ALIVE_TIME = Duration.ofSeconds(60);
    private static final Duration DEFAULT_SHUTDOWN_TIMEOUT = Duration.ofSeconds(2);

    /**
     * Maximum number of threads in the delivery executor.
     */
    @Min(1)
    private int maxThreads = DEFAULT_MAX_THREADS;

    /**
     * Minimum number of threads in the delivery executor.
     */
    @Min(1)
    private int minThreads = DEFAULT_MIN_THREADS;

    /**
     * Capacity of the executor queue holding pending update tasks.
     */
    @Min(1)
    @Max(Integer.MAX_VALUE)
    private int queueCapacity = DEFAULT_QUEUE_CAPACITY;

    /**
     * Time for which idle threads are kept alive before being terminated.
     */
    private Duration threadAliveTime = DEFAULT_THREAD_ALIVE_TIME;

    /**
     * Maximum time to wait for executor shutdown during application stop.
     */
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
