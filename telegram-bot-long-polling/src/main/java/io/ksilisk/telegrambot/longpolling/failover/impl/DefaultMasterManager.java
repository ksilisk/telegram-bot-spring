package io.ksilisk.telegrambot.longpolling.failover.impl;

import io.ksilisk.telegrambot.core.executor.resolver.SwitchableTelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.longpolling.failover.MasterManager;
import io.ksilisk.telegrambot.longpolling.failover.MasterSwitchPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMasterManager implements MasterManager {
    private static final Logger log = LoggerFactory.getLogger(DefaultMasterManager.class);

    private final MasterSwitchPolicy switchPolicy;
    private final SwitchableTelegramBotApiUrlProvider apiUrlProvider;

    public DefaultMasterManager(MasterSwitchPolicy switchPolicy, SwitchableTelegramBotApiUrlProvider apiUrlProvider) {
        this.switchPolicy = switchPolicy;
        this.apiUrlProvider = apiUrlProvider;
    }

    @Override
    public synchronized void recordSuccess() {
        switchPolicy.recordSuccess();
    }

    @Override
    public synchronized void recordFailure(Throwable throwable) {
        switchPolicy.recordFailure(throwable);
        if (!switchPolicy.shouldSwitch()) {
            log.debug("Long polling failure recorded, master switch condition is not met yet: {}", throwable.toString());
            return;
        }
        apiUrlProvider.switchToNext();
        switchPolicy.reset();
    }
}
