package io.ksilisk.telegrambot.core.delivery;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.dispatcher.UpdateDispatcher;
import io.ksilisk.telegrambot.core.handler.exception.CompositeExceptionHandler;
import io.ksilisk.telegrambot.core.interceptor.CompositeUpdateInterceptor;
import io.ksilisk.telegrambot.core.properties.DeliveryProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultUpdateDelivery implements UpdateDelivery {
    private static final Logger log = LoggerFactory.getLogger(DefaultUpdateDelivery.class);

    private final UpdateDispatcher updateDispatcher;
    private final ExecutorService executorService;
    private final DeliveryProperties deliveryProperties;
    private final CompositeUpdateInterceptor compositeUpdateInterceptor;
    private final CompositeExceptionHandler exceptionHandler;

    public DefaultUpdateDelivery(UpdateDispatcher updateDispatcher,
                                 ExecutorService executorService,
                                 DeliveryProperties deliveryProperties,
                                 CompositeUpdateInterceptor compositeUpdateInterceptor,
                                 CompositeExceptionHandler exceptionHandler) {
        this.updateDispatcher = updateDispatcher;
        this.executorService = executorService;
        this.deliveryProperties = deliveryProperties;
        this.compositeUpdateInterceptor = compositeUpdateInterceptor;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void deliver(List<Update> polledUpdates) {
        for (Update update : polledUpdates) {
            executorService.submit(() -> processUpdate(update));
        }
    }

    private void processUpdate(Update update) {
        try {
            Update intercepted = compositeUpdateInterceptor.intercept(update);
            if (intercepted == null) {
                return;
            }
            updateDispatcher.dispatch(update);
        } catch (Exception ex) {
            exceptionHandler.handle(ex, update);
        }
    }

    @Override
    public void close() {
        log.info("Stopping Update delivery");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(
                    deliveryProperties.getShutdownTimeout().toMillis(), TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            executorService.shutdownNow();
        }
    }
}
