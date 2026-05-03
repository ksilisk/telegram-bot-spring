package io.ksilisk.telegrambot.longpolling.failover.impl;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;

class DefaultMasterFailureClassifierTest {

    private final DefaultMasterFailureClassifier classifier = new DefaultMasterFailureClassifier();

    @Test
    void shouldReturnTrueForIOException() {
        IOException exception = new IOException("connection failed");

        assertThat(classifier.isFailure(exception)).isTrue();
    }

    @Test
    void shouldReturnTrueWhenCauseIsIOException() {
        RuntimeException exception = new RuntimeException(
                "request failed",
                new IOException("connection failed")
        );

        assertThat(classifier.isFailure(exception)).isTrue();
    }

    @Test
    void shouldReturnTrueWhenNestedCauseIsIOException() {
        RuntimeException exception = new RuntimeException(
                "outer",
                new IllegalStateException(
                        "middle",
                        new IOException("connection failed")
                )
        );

        assertThat(classifier.isFailure(exception)).isTrue();
    }

    @Test
    void shouldReturnFalseForNonIOException() {
        RuntimeException exception = new RuntimeException("bad request");

        assertThat(classifier.isFailure(exception)).isFalse();
    }

    @Test
    void shouldReturnFalseWhenCauseChainDoesNotContainIOException() {
        RuntimeException exception = new RuntimeException(
                "outer",
                new IllegalStateException("middle")
        );

        assertThat(classifier.isFailure(exception)).isFalse();
    }

    @Test
    void shouldReturnFalseForNull() {
        assertThat(classifier.isFailure(null)).isFalse();
    }
}
