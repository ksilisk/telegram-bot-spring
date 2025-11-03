package io.ksilisk.telegrambot.core.store;

import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryOffsetStore implements OffsetStore {
    private final AtomicInteger currentOffset = new AtomicInteger(0);

    @Override
    public OptionalInt read() {
        return OptionalInt.of(currentOffset.get());
    }

    @Override
    public void write(int offset) {
        currentOffset.set(offset);
    }

    @Override
    public void clear() {
        currentOffset.set(0);
    }
}
