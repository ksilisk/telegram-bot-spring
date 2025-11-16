package io.ksilisk.telegrambot.core.interceptor;

/**
 * Intercepts and optionally transforms a value as it passes through
 * the processing pipeline.
 *
 * <p>An interceptor may:</p>
 * <ul>
 *   <li>return the input unchanged,</li>
 *   <li>return a modified value, or</li>
 *   <li>return {@code null} to indicate that the value should be dropped
 *       and not passed to the next stage.</li>
 * </ul>
 *
 * <p>Interceptors may be invoked concurrently from multiple threads and
 * therefore must be thread-safe.</p>
 *
 * @param <U> the type of value being intercepted
 */
public interface Interceptor<U> {
    /**
     * Intercept the given input.
     *
     * @param input the value to intercept, never {@code null}
     * @return the transformed value, or {@code null} to stop further processing
     */
    U intercept(U input);
}
