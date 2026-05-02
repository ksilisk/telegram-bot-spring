package io.ksilisk.telegrambot.longpolling.executor;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import io.ksilisk.telegrambot.core.exception.request.TelegramRequestException;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.longpolling.failover.MasterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link TelegramBotExecutor} decorator that reports request results to a {@link MasterManager}.
 *
 * <p>Intended for long polling requests only. Successful executions reset the master switch policy,
 * while failures are reported as possible switch candidates.</p>
 */
public final class ReportingMasterAwareTelegramBotExecutor implements TelegramBotExecutor {
    private static final Logger log = LoggerFactory.getLogger(ReportingMasterAwareTelegramBotExecutor.class);

    private final TelegramBotExecutor delegate;
    private final MasterManager masterManager;

    public ReportingMasterAwareTelegramBotExecutor(TelegramBotExecutor delegate, MasterManager masterManager) {
        this.delegate = delegate;
        this.masterManager = masterManager;
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request)
            throws TelegramRequestException {
        try {
            R response = delegate.execute(request);
            masterManager.recordSuccess();
            return response;
        } catch (RuntimeException ex) {
            log.debug("Long polling request failed, reporting failure to master manager", ex);
            masterManager.recordFailure(ex);
            throw ex;
        }
    }
}
