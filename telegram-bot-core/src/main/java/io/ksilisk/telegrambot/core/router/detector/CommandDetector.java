package io.ksilisk.telegrambot.core.router.detector;

import java.util.Optional;

/**
 * Detects a Telegram command in a raw message text.
 *
 * <p>If a valid command is found (e.g. {@code "/start"}), it is returned
 * without additional parsing or normalization. If the message does not
 * represent a command, {@link Optional#empty()} is returned.</p>
 */
public interface CommandDetector {
    /**
     * Attempt to extract a command from the given message text.
     *
     * @param message the raw message text, may be {@code null}
     * @return an {@link Optional} containing the detected command,
     *         or {@code Optional.empty()} if none is found
     */
    Optional<String> detectCommand(String message);
}
