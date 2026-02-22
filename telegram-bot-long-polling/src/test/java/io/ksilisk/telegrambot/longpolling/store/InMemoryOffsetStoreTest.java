package io.ksilisk.telegrambot.longpolling.store;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class InMemoryOffsetStoreTest {
    @Test
    void shouldReturnZeroInitially() {
        InMemoryOffsetStore store = new InMemoryOffsetStore();

        int result = store.read();

        assertEquals(0, result, "Initial offset should be 0");
    }

    @Test
    void shouldStoreAndReturnWrittenOffset() {
        InMemoryOffsetStore store = new InMemoryOffsetStore();

        store.write(42);

        int result = store.read();

        assertEquals(42, result, "Stored offset should be returned");
    }

    @Test
    void shouldOverridePreviousOffsetOnWrite() {
        InMemoryOffsetStore store = new InMemoryOffsetStore();

        store.write(10);
        store.write(99);

        int result = store.read();

        assertEquals(99, result, "Last written offset should win");
    }

    @Test
    void shouldResetOffsetToZeroOnClear() {
        InMemoryOffsetStore store = new InMemoryOffsetStore();

        store.write(123);
        store.clear();

        int result = store.read();

        assertEquals(0, result, "Clear should reset offset to 0");
    }
}
