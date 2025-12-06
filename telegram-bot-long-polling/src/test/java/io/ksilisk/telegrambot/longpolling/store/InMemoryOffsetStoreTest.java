package io.ksilisk.telegrambot.longpolling.store;

import org.junit.jupiter.api.Test;

import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryOffsetStoreTest {
    @Test
    void shouldReturnZeroInitially() {
        InMemoryOffsetStore store = new InMemoryOffsetStore();

        OptionalInt result = store.read();

        assertTrue(result.isPresent(), "Offset should always be present");
        assertEquals(0, result.getAsInt(), "Initial offset should be 0");
    }

    @Test
    void shouldStoreAndReturnWrittenOffset() {
        InMemoryOffsetStore store = new InMemoryOffsetStore();

        store.write(42);

        OptionalInt result = store.read();

        assertTrue(result.isPresent(), "Offset should always be present");
        assertEquals(42, result.getAsInt(), "Stored offset should be returned");
    }

    @Test
    void shouldOverridePreviousOffsetOnWrite() {
        InMemoryOffsetStore store = new InMemoryOffsetStore();

        store.write(10);
        store.write(99);

        OptionalInt result = store.read();

        assertTrue(result.isPresent(), "Offset should always be present");
        assertEquals(99, result.getAsInt(), "Last written offset should win");
    }

    @Test
    void shouldResetOffsetToZeroOnClear() {
        InMemoryOffsetStore store = new InMemoryOffsetStore();

        store.write(123);
        store.clear();

        OptionalInt result = store.read();

        assertTrue(result.isPresent(), "Offset should always be present");
        assertEquals(0, result.getAsInt(), "Clear should reset offset to 0");
    }
}
