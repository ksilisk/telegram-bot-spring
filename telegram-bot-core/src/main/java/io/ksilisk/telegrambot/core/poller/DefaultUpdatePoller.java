package io.ksilisk.telegrambot.core.poller;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import io.ksilisk.telegrambot.core.delivery.UpdateDelivery;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.properties.LongPollingProperties;
import io.ksilisk.telegrambot.core.store.OffsetStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class DefaultUpdatePoller implements UpdatePoller, AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(DefaultUpdatePoller.class);

    private final OffsetStore offsetStore;
    private final TelegramBotExecutor telegramBotExecutor;
    private final UpdateDelivery updateDelivery;
    private final LongPollingProperties properties;

    private ExecutorService executorService = buildExecutorService();
    private volatile boolean running = false;

    public DefaultUpdatePoller(OffsetStore offsetStore,
                               TelegramBotExecutor telegramBotExecutor,
                               UpdateDelivery updateDelivery,
                               LongPollingProperties pollingProperties) {
        this.offsetStore = offsetStore;
        this.telegramBotExecutor = telegramBotExecutor;
        this.updateDelivery = updateDelivery;
        this.properties = pollingProperties;
    }

    @Override
    public synchronized void start() {
        log.info("Starting Telegram Update Poller");
        if (!running) {
            if (executorService.isShutdown() || executorService.isTerminated()) {
                executorService = buildExecutorService();
            }
            executorService.submit(this::runLoop);
            running = true;
        } else {
            log.warn("Telegram Update Poller is already started");
        }
    }

    @Override
    public synchronized void stop() {
        log.info("Stopping Telegram Update Poller");
        if (running) {
            running = false;
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(properties.getShutdownTimeout().getSeconds(), TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                executorService.shutdownNow();
            }
        }
    }

    @Override
    public void close() {
        stop();
    }

    private void runLoop() {
        try {
            if (properties.getDropPendingOnStart()) {
                drainPendingUpdates();
            }
            GetUpdates getUpdates = new GetUpdates();
            while (running) {
                updateGetUpdatesRequest(getUpdates);
                GetUpdatesResponse getUpdatesResponse;
                try {
                    getUpdatesResponse = telegramBotExecutor.execute(getUpdates);
                } catch (Exception ex) {
                    if (!running || Thread.currentThread().isInterrupted()) {
                        break;
                    }
                    log.warn("Error while executing GetUpdates request", ex);

                    try {
                        Thread.sleep(properties.getRetryDelay().toMillis());
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    continue;
                }

                if (getUpdatesResponse == null) {
                    continue;
                }

                if (!getUpdatesResponse.isOk()) {
                    log.warn("GetUpdates request failed. Code: {}, Description: {}",
                            getUpdatesResponse.errorCode(), getUpdatesResponse.description());
                    continue;
                }

                List<Update> updates = getUpdatesResponse.updates();
                log.debug("Received {} updates from Telegram Bot API", updates.size());

                try {
                    if (!updates.isEmpty()) {
                        updateDelivery.deliver(updates);
                        int lastUpdateId = updates.get(updates.size() - 1).updateId();
                        offsetStore.write(lastUpdateId + 1);
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch (Exception ex) {
            log.error("Error while launching Telegram Update Poller loop", ex);
        }
    }

    private void drainPendingUpdates() {
        try {
            GetUpdates getUpdates = new GetUpdates();
            GetUpdatesResponse getUpdatesResponse = telegramBotExecutor.execute(getUpdates);
            if (!getUpdatesResponse.updates().isEmpty()) {
                int lastUpdateId = getUpdatesResponse.updates().get(getUpdatesResponse.updates().size() - 1).updateId();
                offsetStore.write(lastUpdateId + 1);
            }
        } catch (Exception ex) {
            log.warn("Error while draining pending updates on start", ex);
        }
    }

    private void updateGetUpdatesRequest(GetUpdates getUpdates) {
        int offset = offsetStore.read().orElse(0);

        if (properties.getAllowedUpdates() != null && !properties.getAllowedUpdates().isEmpty()) {
            getUpdates.allowedUpdates(properties.getAllowedUpdates().toArray(String[]::new));
        }
        getUpdates.offset(offset);
        getUpdates.timeout((int) properties.getTimeout().getSeconds());
        getUpdates.limit(properties.getLimit());
    }

    private ExecutorService buildExecutorService() {
        return newSingleThreadExecutor(r -> new Thread(r, "telegram-poller"));
    }
}
