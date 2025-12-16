package io.ksilisk.telegrambot.observability.metrics;

import java.util.Locale;

/**
 * Logical channels used to classify update handling for observability purposes.
 *
 * <p>Channels are used as low-cardinality metric tags and do not affect
 * the update processing logic.</p>
 */
public enum TelegramBotChannel {
    COMMAND,
    CALLBACK,
    MESSAGE,
    INLINE,
    OTHER;

    public String tagValue() {
        return name().toLowerCase(Locale.ROOT);
    }
}
