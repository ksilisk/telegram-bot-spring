package io.ksilisk.telegrambot.core.ingress.impl;

import io.ksilisk.telegrambot.core.ingress.LongPollingUpdateIngress;
import io.ksilisk.telegrambot.core.poller.UpdatePoller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLongPollingUpdateIngress implements LongPollingUpdateIngress {
    private static final Logger log = LoggerFactory.getLogger(DefaultLongPollingUpdateIngress.class);

    private final UpdatePoller updatePoller;

    public DefaultLongPollingUpdateIngress(UpdatePoller updatePoller) {
        this.updatePoller = updatePoller;
    }

    @Override
    public void start() {
        log.info("Starting Long-Polling ingress");
        updatePoller.start();
    }

    @Override
    public void stop() {
        log.info("Stopping Long-Polling ingress");
        updatePoller.stop();
    }
}
