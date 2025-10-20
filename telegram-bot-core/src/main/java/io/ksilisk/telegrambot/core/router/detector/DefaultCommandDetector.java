package io.ksilisk.telegrambot.core.router.detector;

import java.util.Optional;
/**
 * Default implementation of CommandDetector for Telegram bots.
 *
 * Rules based on Telegram Bot API:
 * - A command must start at the very beginning of the message.
 * - If there is any whitespace or newline before '/', it is NOT a valid command.
 * - Commands can start with "/" or "@".
 * - Valid examples: "/start", "/start@mybot", "/help arg1"
 * - Invalid examples: " /start", " hello /start", "\n/start"
 */
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

		// Command must start with '/' or '@'
		if (!message.startsWith("/") && !message.startsWith("@")) {
			return Optional.empty();
		}

		// Extract first token before any space
		String[] parts = message.split("\\s+", 2);
		String firstWord = parts[0];

		boolean isValid = firstWord.matches("^[/@][a-zA-Z0-9_]+(@[a-zA-Z0-9_]+)?$");
		if (!isValid) {
			return Optional.empty();
		}

		return Optional.of(firstWord);
	}
}

