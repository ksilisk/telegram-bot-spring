package io.ksilisk.telegrambot.longpolling.failover.policy.duration;

import io.ksilisk.telegrambot.longpolling.failover.MasterFailureClassifier;
import io.ksilisk.telegrambot.longpolling.failover.MasterSwitchPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * {@link MasterSwitchPolicy} that switches master after failures continue for a configured duration.
 *
 * <p>A successful operation resets the failure window.</p>
 */
public class DurationBasedMasterSwitchPolicy implements MasterSwitchPolicy {
    private static final Logger log = LoggerFactory.getLogger(DurationBasedMasterSwitchPolicy.class);

    private final MasterFailureClassifier failureClassifier;
    private final Duration switchAfter;
    private final Clock clock;

    private Instant firstFailureAt;
    private boolean switchRequired;

    public DurationBasedMasterSwitchPolicy(MasterFailureClassifier failureClassifier, Duration switchAfter) {
        this(failureClassifier, switchAfter, Clock.systemUTC());
    }

    DurationBasedMasterSwitchPolicy(MasterFailureClassifier failureClassifier, Duration switchAfter, Clock clock) {
        this.failureClassifier = Objects.requireNonNull(failureClassifier);
        this.switchAfter = Objects.requireNonNull(switchAfter);
        this.clock = Objects.requireNonNull(clock);

        if (switchAfter.isNegative()) {
            throw new IllegalArgumentException("switchAfter must not be negative");
        }
    }

    @Override
    public void recordSuccess() {
        reset();
    }

    @Override

    public void recordFailure(Throwable throwable) {
        if (!failureClassifier.isFailure(throwable)) {
            log.debug("Long polling failure ignored by classifier: {}", String.valueOf(throwable));
            return;
        }
        Instant now = Instant.now(clock);
        if (firstFailureAt == null) {
            firstFailureAt = now;
            log.debug("Long polling failure window opened");
        }
        Duration failureDuration = Duration.between(firstFailureAt, now);

        if (!failureDuration.minus(switchAfter).isNegative()) {
            switchRequired = true;
            log.debug("Long polling switch required: failureDuration={}, switchAfter={}", failureDuration, switchAfter);
        }
    }

    @Override
    public boolean shouldSwitch() {
        return switchRequired;
    }

    @Override
    public void reset() {
        firstFailureAt = null;
        switchRequired = false;
        log.debug("Long polling master switch policy reset");
    }
}
