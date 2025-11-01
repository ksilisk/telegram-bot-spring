package io.ksilisk.telegrambot.core.router.detector;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultCommandDetectorTest {
    private final DefaultCommandDetector detector = new DefaultCommandDetector();

    @Test
    void shouldReturnEmptyWhenMessageIsNull() {
        Optional<String> result = detector.detectCommand(null);
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenMessageIsEmpty() {
        Optional<String> result = detector.detectCommand("");
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenMessageStartsWithWhitespace() {
        Optional<String> result = detector.detectCommand(" /start");
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenMessageDoesNotStartWithSlash() {
        Optional<String> result = detector.detectCommand("start");
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenCommandContainsInvalidCharacters() {
        Optional<String> result = detector.detectCommand("/sta@rt");
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnCommandWhenValid() {
        Optional<String> result = detector.detectCommand("/start");
        assertThat(result).contains("/start");
    }

    @Test
    void shouldReturnOnlyFirstTokenWhenCommandHasArguments() {
        Optional<String> result = detector.detectCommand("/start arg1 arg2");
        assertThat(result).contains("/start");
    }

    @Test
    void shouldReturnEmptyWhenCommandContainsOnlySlash() {
        Optional<String> result = detector.detectCommand("/");
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnCommandWhenContainsUnderscoreOrDigits() {
        assertThat(detector.detectCommand("/start_123")).contains("/start_123");
        assertThat(detector.detectCommand("/cmd2")).contains("/cmd2");
    }
}