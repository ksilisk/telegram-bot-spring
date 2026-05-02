package io.ksilisk.telegrambot.longpolling.failover;

/**
 * Classifies failures that should contribute to master endpoint switching.
 */
public interface MasterFailureClassifier {
    /**
     * Returns whether the given failure should be treated as a master endpoint failure.
     *
     * @param throwable failure to classify
     * @return {@code true} if the failure should affect failover state
     */
    boolean isFailure(Throwable throwable);
}
