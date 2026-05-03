package io.ksilisk.telegrambot.longpolling.failover.policy.duration;

import io.ksilisk.telegrambot.longpolling.failover.MasterFailureClassifier;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DurationBasedMasterSwitchPolicyTest {

    private static final Instant NOW = Instant.parse("2026-05-03T10:00:00Z");

    @Test
    void shouldRejectNegativeSwitchAfter() {
        MasterFailureClassifier classifier = throwable -> true;

        assertThatThrownBy(() -> new DurationBasedMasterSwitchPolicy(
                classifier,
                Duration.ofMillis(-1),
                fixedClock(NOW)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("switchAfter must not be negative");
    }

    @Test
    void shouldNotSwitchWhenFailureIsIgnoredByClassifier() {
        DurationBasedMasterSwitchPolicy policy = new DurationBasedMasterSwitchPolicy(
                throwable -> false,
                Duration.ZERO,
                fixedClock(NOW)
        );

        policy.recordFailure(new IOException("connection failed"));

        assertThat(policy.shouldSwitch()).isFalse();
    }

    @Test
    void shouldSwitchImmediatelyWhenSwitchAfterIsZero() {
        DurationBasedMasterSwitchPolicy policy = new DurationBasedMasterSwitchPolicy(
                throwable -> true,
                Duration.ZERO,
                fixedClock(NOW)
        );

        policy.recordFailure(new IOException("connection failed"));

        assertThat(policy.shouldSwitch()).isTrue();
    }

    @Test
    void shouldNotSwitchBeforeConfiguredDurationPasses() {
        MutableClock clock = new MutableClock(NOW);

        DurationBasedMasterSwitchPolicy policy = new DurationBasedMasterSwitchPolicy(
                throwable -> true,
                Duration.ofSeconds(60),
                clock
        );

        policy.recordFailure(new IOException("connection failed"));
        assertThat(policy.shouldSwitch()).isFalse();

        clock.advance(Duration.ofSeconds(59));
        policy.recordFailure(new IOException("connection failed"));

        assertThat(policy.shouldSwitch()).isFalse();
    }

    @Test
    void shouldSwitchWhenConfiguredDurationPasses() {
        MutableClock clock = new MutableClock(NOW);

        DurationBasedMasterSwitchPolicy policy = new DurationBasedMasterSwitchPolicy(
                throwable -> true,
                Duration.ofSeconds(60),
                clock
        );

        policy.recordFailure(new IOException("connection failed"));
        assertThat(policy.shouldSwitch()).isFalse();

        clock.advance(Duration.ofSeconds(60));
        policy.recordFailure(new IOException("connection failed"));

        assertThat(policy.shouldSwitch()).isTrue();
    }

    @Test
    void shouldResetSwitchStateOnSuccess() {
        DurationBasedMasterSwitchPolicy policy = new DurationBasedMasterSwitchPolicy(
                throwable -> true,
                Duration.ZERO,
                fixedClock(NOW)
        );

        policy.recordFailure(new IOException("connection failed"));
        assertThat(policy.shouldSwitch()).isTrue();

        policy.recordSuccess();

        assertThat(policy.shouldSwitch()).isFalse();
    }

    @Test
    void shouldResetSwitchStateOnReset() {
        DurationBasedMasterSwitchPolicy policy = new DurationBasedMasterSwitchPolicy(
                throwable -> true,
                Duration.ZERO,
                fixedClock(NOW)
        );

        policy.recordFailure(new IOException("connection failed"));
        assertThat(policy.shouldSwitch()).isTrue();

        policy.reset();

        assertThat(policy.shouldSwitch()).isFalse();
    }

    @Test
    void shouldOpenNewFailureWindowAfterReset() {
        MutableClock clock = new MutableClock(NOW);

        DurationBasedMasterSwitchPolicy policy = new DurationBasedMasterSwitchPolicy(
                throwable -> true,
                Duration.ofSeconds(60),
                clock
        );

        policy.recordFailure(new IOException("first failure"));
        clock.advance(Duration.ofSeconds(30));
        policy.reset();

        policy.recordFailure(new IOException("second failure"));
        clock.advance(Duration.ofSeconds(30));
        policy.recordFailure(new IOException("third failure"));

        assertThat(policy.shouldSwitch()).isFalse();

        clock.advance(Duration.ofSeconds(30));
        policy.recordFailure(new IOException("fourth failure"));

        assertThat(policy.shouldSwitch()).isTrue();
    }

    private static Clock fixedClock(Instant instant) {
        return Clock.fixed(instant, ZoneOffset.UTC);
    }

    private static final class MutableClock extends Clock {

        private Instant instant;

        private MutableClock(Instant instant) {
            this.instant = instant;
        }

        private void advance(Duration duration) {
            instant = instant.plus(duration);
        }

        @Override
        public ZoneOffset getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(java.time.ZoneId zone) {
            return Clock.fixed(instant, zone);
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
