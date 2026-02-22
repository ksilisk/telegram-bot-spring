package io.ksilisk.telegrambot.core.mdc;

import org.slf4j.MDC;

import java.util.Map;

/**
 * Captures the current SLF4J {@link MDC} context map and propagates it to another thread.
 *
 * <p>Useful for thread pools where {@code MDC} does not flow automatically: capture the context in the
 * submitting thread and {@link #wrap(Runnable) wrap} the task so the captured context is installed
 * during execution and the previous worker-thread context is restored afterwards.</p>
 *
 * <p>When no context is present, {@link #wrap(Runnable)} returns the original task unchanged.</p>
 */
public final class MDCSnapshot {
    private final Map<String, String> captured;

    private MDCSnapshot(Map<String, String> captured) {
        this.captured = captured;
    }

    public static MDCSnapshot capture() {
        Map<String, String> map = MDC.getCopyOfContextMap();
        return new MDCSnapshot(map);
    }

    public boolean isEmpty() {
        return captured == null;
    }

    public Runnable wrap(Runnable task) {
        if (isEmpty()) {
            return task;
        }
        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            try {
                MDC.setContextMap(captured);
                task.run();
            } finally {
                if (previous == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(previous);
                }
            }
        };
    }
}
