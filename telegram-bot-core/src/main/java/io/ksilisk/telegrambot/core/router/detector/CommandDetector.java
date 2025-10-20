package io.ksilisk.telegrambot.core.router.detector;

import java.util.Optional;

public interface CommandDetector {
	Optional<String> detectCommand(String message);
}

