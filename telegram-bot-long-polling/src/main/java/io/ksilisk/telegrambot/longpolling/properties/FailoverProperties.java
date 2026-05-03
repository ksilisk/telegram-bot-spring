package io.ksilisk.telegrambot.longpolling.properties;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Long polling failover configuration.
 *
 * <p>Failover is driven by long polling failures and switches the active Telegram Bot API
 * endpoint used by the application.</p>
 */
public class FailoverProperties {
    private static final boolean DEFAULT_ENABLED = true;
    private static final Policy DEFAULT_POLICY = Policy.DURATION;

    /**
     * Master switch policy type.
     */
    public enum Policy {
        /**
         * Switches master after failures continue for a configured duration.
         */
        DURATION
    }

    /**
     * Whether long polling failover is enabled.
     */
    private boolean enabled = DEFAULT_ENABLED;

    /**
     * Policy used to decide when the current master should be switched.
     */
    private Policy policy = DEFAULT_POLICY;

    /**
     * Duration-based failover settings.
     */
    @NestedConfigurationProperty
    private DurationFailoverProperties duration = new DurationFailoverProperties();

    public DurationFailoverProperties getDuration() {
        return duration;
    }

    public void setDuration(DurationFailoverProperties duration) {
        this.duration = duration;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }
}
