package io.ksilisk.telegrambot.observability.metrics;

import io.micrometer.core.instrument.Tags;

import java.util.Locale;

/**
 * Defines built-in observability metrics for the Telegram bot.
 *
 * <p>Each metric describes its semantic meaning and is used by the
 * observability module when recording counters, timers, summaries and gauges.</p>
 */
public enum TelegramBotMetric {
    UPDATES_RECEIVED(
            "telegram.bot.updates.received",
            "Total number of updates received by the delivery layer. For batch deliveries, the batch size is summed."
    ),

    UPDATES_BATCH_SIZE(
            "telegram.bot.updates.batch_size",
            "Size of update batches passed to UpdateDelivery.deliver(..)."
    ),

    ROUTING_NO_MATCH(
            "telegram.bot.routing.no_match",
            "Total number of updates that did not match any routing rule/handler and were handled by the no-match strategy."
    ),

    HANDLER_DURATION(
            "telegram.bot.handler.duration",
            "Time spent executing UpdateHandler.handle(..). Recorded per handler invocation."
    ),

    HANDLER_INVOCATIONS(
            "telegram.bot.handler.invocations",
            "Total number of UpdateHandler.handle(..) invocations."
    ),

    API_DURATION(
            "telegram.bot.api.duration",
            "Time spent executing TelegramBotExecutor.execute(..). Recorded per Telegram API call."
    ),

    API_CALLS(
            "telegram.bot.api.calls",
            "Total number of TelegramBotExecutor.execute(..) calls."
    ),

    EXCEPTIONS_TOTAL(
            "telegram.bot.exceptions.total",
            "Total number of exceptions observed by the update processing pipeline " +
                    "(typically counted when exception handlers are invoked)."
    ),

    DELIVERY_POOL_SIZE(
            "telegram.bot.delivery.pool.size",
            "Current number of threads in the update delivery ThreadPoolExecutor."
    ),

    DELIVERY_POOL_ACTIVE(
            "telegram.bot.delivery.pool.active",
            "Current number of actively executing threads in the update delivery ThreadPoolExecutor."
    ),

    DELIVERY_POOL_QUEUE_SIZE(
            "telegram.bot.delivery.pool.queue.size",
            "Current number of queued update-processing tasks in the update delivery ThreadPoolExecutor queue."
    );

    public static final String TAG_STATUS = "status";
    public static final String TAG_HANDLER = "handler";
    public static final String TAG_CHANNEL = "channel";
    public static final String TAG_METHOD = "method";
    public static final String TAG_EXCEPTION = "exception";

    private final String name;
    private final String description;

    TelegramBotMetric(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String metricName() {
        return name;
    }

    public String description() {
        return description;
    }

    public enum Status {
        SUCCESS, ERROR;

        public String tagValue() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    public static Tags status(Status status) {
        return Tags.of(TAG_STATUS, status.tagValue());
    }

    public static Tags handler(String handlerId) {
        return Tags.of(TAG_HANDLER, sanitize(handlerId));
    }

    public static Tags channel(TelegramBotChannel channel) {
        return Tags.of(TAG_CHANNEL, channel.tagValue());
    }

    public static Tags method(String method) {
        return Tags.of(TAG_METHOD, sanitize(method));
    }

    public static Tags exception(Throwable error) {
        return Tags.of(TAG_EXCEPTION, error == null ? "unknown" : error.getClass().getSimpleName());
    }

    public static Tags handlerChannel(String handlerId, TelegramBotChannel channel) {
        return handler(handlerId).and(channel(channel));
    }

    private static String sanitize(String value) {
        if (value == null || value.isBlank()) {
            return "unknown";
        }
        return value.trim().replace(' ', '_');
    }
}
