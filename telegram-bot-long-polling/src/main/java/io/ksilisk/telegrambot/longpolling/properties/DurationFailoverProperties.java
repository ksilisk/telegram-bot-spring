package io.ksilisk.telegrambot.longpolling.properties;

import java.time.Duration;

/**
 * Duration-based failover policy settings.
 */
public class DurationFailoverProperties {
    private static final Duration DEFAULT_SWITCH_AFTER = Duration.ofSeconds(60);

    /**
     * Continuous failure duration required before switching the current master endpoint.
     */
    private Duration switchAfter = DEFAULT_SWITCH_AFTER;

    public Duration getSwitchAfter() {
        return switchAfter;
    }

    public void setSwitchAfter(Duration switchAfter) {
        this.switchAfter = switchAfter;
    }
}
