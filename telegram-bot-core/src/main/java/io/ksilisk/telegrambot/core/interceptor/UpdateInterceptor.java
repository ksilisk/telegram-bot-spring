package io.ksilisk.telegrambot.core.interceptor;

import com.pengrad.telegrambot.model.Update;

/**
 * Interceptor for Telegram {@link Update} objects.
 *
 * <p>Executed between delivery and dispatching. May transform the update
 * or return {@code null} to drop it and prevent further processing.</p>
 *
 * <p>Update interceptors may be invoked concurrently by the delivery
 * thread pool and therefore must be thread-safe.</p>
 */
public interface UpdateInterceptor extends Interceptor<Update> {
}
