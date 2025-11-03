package io.ksilisk.telegrambot.core.delivery;

import com.pengrad.telegrambot.model.Update;

import java.util.List;

public interface UpdateDelivery extends AutoCloseable {
    void deliver(List<Update> polledUpdates) throws InterruptedException;
}
