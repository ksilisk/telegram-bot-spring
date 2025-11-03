package io.ksilisk.telegrambot.core.delivery;

import io.ksilisk.telegrambot.core.properties.DeliveryProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultDeliveryThreadPoolExecutorFactory implements DeliveryThreadPoolExecutorFactory {
    private final DeliveryProperties deliveryProperties;
    private final RejectedExecutionHandler rejectedExecutionHandler;

    public DefaultDeliveryThreadPoolExecutorFactory(DeliveryProperties deliveryProperties) {
        this(deliveryProperties, new LoggingRejectedExecutionHandler());
    }

    public DefaultDeliveryThreadPoolExecutorFactory(DeliveryProperties deliveryProperties,
                                                    RejectedExecutionHandler rejectedExecutionHandler) {
        this.deliveryProperties = deliveryProperties;
        this.rejectedExecutionHandler = rejectedExecutionHandler;
    }

    @Override
    public ThreadPoolExecutor buildThreadPoolExecutor() {
        return new ThreadPoolExecutor(
                deliveryProperties.getMinThreads(),
                deliveryProperties.getMaxThreads(),
                deliveryProperties.getThreadAliveTime().toMillis(),
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(deliveryProperties.getQueueCapacity()),
                rejectedExecutionHandler);
    }

    public static class LoggingRejectedExecutionHandler implements RejectedExecutionHandler {
        private static final Logger log = LoggerFactory.getLogger(LoggingRejectedExecutionHandler.class);

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.error("Unable to schedule or execute a task '{}'. Active threads: '{}', Queue size: '{}'",
                    r.toString(), executor.getActiveCount(), executor.getQueue().size());
        }
    }
}
