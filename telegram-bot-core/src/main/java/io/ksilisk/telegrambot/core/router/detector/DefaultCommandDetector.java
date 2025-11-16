package io.ksilisk.telegrambot.core.router.detector;

import java.util.Optional;

public class DefaultCommandDetector implements CommandDetector {

    @Override
    public Optional<String> detectCommand(String message) {

        // null check
        if (message == null || message.isEmpty()) {
            return Optional.empty();
        }

        // Leading whitespace → invalid (Telegram commands must start at position 0)
        if (Character.isWhitespace(message.charAt(0))) {
            return Optional.empty();
        }

        // Command must start with '/'
        if (!message.startsWith("/")) {
            return Optional.empty();
        }

        // Extract first token before any space
        String[] parts = message.split("\\s+", 2);
        String firstWord = parts[0];

        // Command validation: must start with '/' followed by valid chars
        boolean isValid = firstWord.matches("^/[a-zA-Z0-9_]+$");
        if (!isValid) {
            return Optional.empty();
        }

        return Optional.of(firstWord);
    }
}
