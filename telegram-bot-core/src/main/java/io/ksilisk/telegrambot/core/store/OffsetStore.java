package io.ksilisk.telegrambot.core.store;

import java.util.OptionalInt;

public interface OffsetStore {
    OptionalInt read();

    void write(int offset);

    void clear();
}
