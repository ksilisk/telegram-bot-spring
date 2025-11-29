package io.ksilisk.telegrambot.core.executor;

/**
 * Abstraction for executing Telegram Bot API operations.
 *
 * <p>Provides a common contract for components that perform outbound calls to
 * the Telegram Bot API, regardless of the underlying HTTP client or library.</p>
 *
 * <p>Specific executors may expose additional methods for request execution
 * (e.g. {@code execute(BaseRequest)}, reactive APIs, batching, etc.).</p>
 */
public interface ApiExecutor {
}
