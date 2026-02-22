package io.ksilisk.telegrambot.core.mdc;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.update.Updates;
import org.slf4j.MDC;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

/**
 * Populates SLF4J {@link MDC} with context extracted from a Telegram {@link Update}.
 *
 * <p>Sets the following keys when available:
 * <ul>
 *   <li>{@value #UPDATE_ID}</li>
 *   <li>{@value #UPDATE_TYPE}</li>
 *   <li>{@value #USER_ID}</li>
 *   <li>{@value #CHAT_ID}</li>
 * </ul>
 *
 * <p>Use with try-with-resources to avoid MDC leakage in thread pools:
 * <pre>{@code
 * try (var ignored = UpdateMDC.open(update)) {
 *   process(update);
 * }
 * }</pre>
 *
 * <p>On close, previously stored values for the same keys are restored.</p>
 */
public class UpdateMDC {
    public static final String UPDATE_ID = "update_id";
    public static final String UPDATE_TYPE = "update_type";
    public static final String USER_ID = "user_id";
    public static final String CHAT_ID = "chat_id";

    private UpdateMDC() {
    }

    public static AutoCloseable open(Update update) {
        if (update == null) {
            return NoopCloseable.INSTANCE;
        }

        List<MDC.MDCCloseable> closeables = new ArrayList<>(4);

        put(closeables, UPDATE_ID, update.updateId());
        put(closeables, UPDATE_TYPE, Updates.type(update).name());
        put(closeables, USER_ID, Updates.userId(update));
        put(closeables, CHAT_ID, Updates.chatId(update));

        if (closeables.isEmpty()) {
            return NoopCloseable.INSTANCE;
        }

        return () -> {
            for (int i = closeables.size() - 1; i >= 0; i--) {
                closeables.get(i).close();
            }
        };
    }

    private static void put(List<MDC.MDCCloseable> closeables, String key, Object value) {
        if (value == null) {
            return;
        }
        String s = String.valueOf(value);

        if (s.isBlank()) {
            return;
        }
        closeables.add(MDC.putCloseable(key, s));
    }

    private enum NoopCloseable implements Closeable {
        INSTANCE;

        @Override
        public void close() {
        }
    }
}
