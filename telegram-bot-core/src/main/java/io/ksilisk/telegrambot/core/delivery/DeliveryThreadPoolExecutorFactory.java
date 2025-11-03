package io.ksilisk.telegrambot.core.delivery;

import java.util.concurrent.ThreadPoolExecutor;

public interface DeliveryThreadPoolExecutorFactory {
    ThreadPoolExecutor buildThreadPoolExecutor();
}
