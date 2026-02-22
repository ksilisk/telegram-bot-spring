package io.ksilisk.telegrambot.core.mdc;

import org.slf4j.MDC;

import java.util.Map;

public class MDCSnapshot {
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
