package io.ksilisk.telegrambot.longpolling.failover;

/**
 * Facade for reporting master endpoint health.
 *
 * <p>The manager hides switch policy and endpoint selection details from callers.</p>
 */
public interface MasterManager {
    /**
     * Facade for reporting master endpoint health.
     *
     * <p>The manager hides switch policy and endpoint selection details from callers.</p>
     */
    void recordSuccess();

    /**
     * Records a failed operation against the current master endpoint.
     *
     * @param throwable failure to evaluate
     */
    void recordFailure(Throwable throwable);
}
