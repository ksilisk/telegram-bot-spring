package io.ksilisk.telegrambot.longpolling.store;

import java.util.OptionalInt;

/**
 * Stores the update offset used by long-polling.
 *
 * <p>An {@link OffsetStore} provides a simple persistence mechanism for the
 * last processed update ID. Implementations may use in-memory storage,
 * files, databases, or any other medium.</p>
 */
public interface OffsetStore {
    /**
     * Read the last stored offset.
     *
     * @return the offset, or {@link OptionalInt#empty()} if none is stored
     */
    int read();

    /**
     * Persist the given offset.
     *
     * @param offset the update ID to store
     */
    void write(int offset);

    /**
     * Clear the stored offset.
     */
    void clear();
}
