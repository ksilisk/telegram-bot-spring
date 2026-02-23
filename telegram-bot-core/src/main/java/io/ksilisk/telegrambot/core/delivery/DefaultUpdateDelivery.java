package io.ksilisk.telegrambot.core.delivery;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.dispatcher.UpdateDispatcher;
import io.ksilisk.telegrambot.core.handler.exception.CompositeUpdateExceptionHandler;
import io.ksilisk.telegrambot.core.interceptor.CompositeUpdateInterceptor;
import io.ksilisk.telegrambot.core.mdc.MDCSnapshot;
import io.ksilisk.telegrambot.core.mdc.UpdateMDC;
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
    private final CompositeUpdateExceptionHandler exceptionHandler;

    public DefaultUpdateDelivery(UpdateDispatcher updateDispatcher,
                                 ExecutorService executorService,
                                 DeliveryProperties deliveryProperties,
                                 CompositeUpdateInterceptor compositeUpdateInterceptor,
                                 CompositeUpdateExceptionHandler exceptionHandler) {
        this.updateDispatcher = updateDispatcher;
        this.executorService = executorService;
        this.deliveryProperties = deliveryProperties;
        this.compositeUpdateInterceptor = compositeUpdateInterceptor;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void deliver(List<Update> polledUpdates) {
        MDCSnapshot capture = MDCSnapshot.capture();

        for (Update update : polledUpdates) {
            Runnable processUpdateWithMDC = () -> {
                try (AutoCloseable ignored = UpdateMDC.open(update)) {
                    processUpdate(update);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            };

            executorService.submit(() -> capture.wrap(processUpdateWithMDC).run());
        }
    }

    @Override
    public void start() {
        // noop
    }

    private void processUpdate(Update update) {
        try {
            Update intercepted = compositeUpdateInterceptor.intercept(update);
            if (intercepted == null) {
                return;
            }
            updateDispatcher.dispatch(update);
        } catch (Exception ex) {
            log.debug("Error while processing update (id={})", update.updateId(), ex);
            exceptionHandler.handle(ex, update);
        }
    }

    public void stop() {
        log.info("Stopping Update Delivery");
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
