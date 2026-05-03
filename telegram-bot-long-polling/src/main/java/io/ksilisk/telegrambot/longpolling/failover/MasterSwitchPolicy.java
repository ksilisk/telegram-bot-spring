package io.ksilisk.telegrambot.longpolling.failover;

/**
 * Policy that decides when the current Telegram Bot API master endpoint should be switched.
 *
 * <p>Implementations may use different strategies, such as duration-based or count-based failure tracking.</p>
 */
public interface MasterSwitchPolicy {
    /**
     * Records a successful interaction with the current master endpoint.
     */
    void recordSuccess();

    /**
     * Records a failed interaction with the current master endpoint.
     *
     * @param throwable failure to evaluate
     */
    void recordFailure(Throwable throwable);

    /**
     * Returns whether the current master endpoint should be switched.
     *
     * @return {@code true} if a switch is required
     */
    boolean shouldSwitch();

    /**
     * Resets the policy state after recovery or after a master switch.
     */
    void reset();
}
