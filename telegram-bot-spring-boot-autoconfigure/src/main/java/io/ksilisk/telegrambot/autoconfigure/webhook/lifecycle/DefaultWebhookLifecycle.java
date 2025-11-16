package io.ksilisk.telegrambot.autoconfigure.webhook.lifecycle;

import com.pengrad.telegrambot.request.DeleteWebhook;
import com.pengrad.telegrambot.request.SetWebhook;
import io.ksilisk.telegrambot.core.exception.webhook.WebhookRegisteringException;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.properties.WebhookProperties;
import io.ksilisk.telegrambot.core.webhook.WebhookLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultWebhookLifecycle implements WebhookLifecycle, SmartLifecycle {
    private static final Logger log = LoggerFactory.getLogger(DefaultWebhookLifecycle.class);

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final WebhookProperties webhookProperties;
    private final TelegramBotExecutor telegramBotExecutor;

    public DefaultWebhookLifecycle(WebhookProperties webhookProperties, TelegramBotExecutor telegramBotExecutor) {
        this.webhookProperties = webhookProperties;
        this.telegramBotExecutor = telegramBotExecutor;
    }

    @Override
    public void register() {
        if (!webhookProperties.getAutoRegister()) {
            log.info("Auto webhook registration is disabled (telegram.webhook.auto-register=false)");
            return;
        }

        if (!StringUtils.hasText(webhookProperties.getExternalUrl())) {
            throw new WebhookRegisteringException("telegram.webhook.url must be set when auto-register is enabled");
        }

        try {
            log.info("Registering Telegram webhook at URL: {}", webhookProperties.getExternalUrl());
            telegramBotExecutor.execute(createSetWebhookRequest());
            log.info("Telegram webhook successfully registered");
        } catch (Exception ex) {
            throw new WebhookRegisteringException("Error while webhook registering", ex);
        }
    }

    @Override
    public void remove() {
        if (!webhookProperties.getAutoRemove()) {
            log.info("Auto webhook removal is disabled (telegram.webhook.auto-remove=false)");
            return;
        }

        try {
            log.info("Deleting Telegram webhook");
            telegramBotExecutor.execute(createDeleteWebhookRequest());
            log.info("Telegram webhook successfully deleted");
        } catch (Exception ex) {
            log.warn("Failed to delete Telegram webhook during shutdown", ex);
        }
    }

    @Override
    public void start() {
        if (!running.compareAndExchange(false, true)) {
            register();
        }
    }

    @Override
    public void stop() {
        if (running.compareAndExchange(true, false)) {
            remove();
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public int getPhase() {
        return DEFAULT_PHASE - 1000; // starts after WEBSERVER (Jetty, Tomcat, etc.)
    }

    private SetWebhook createSetWebhookRequest() {
        SetWebhook setWebhook = new SetWebhook();

        setWebhook.url(webhookProperties.getExternalUrl());
        setWebhook.dropPendingUpdates(webhookProperties.getDropPendingUpdatesOnRegister());
        setWebhook.maxConnections(webhookProperties.getMaxConnections());

        if (webhookProperties.getAllowedUpdates() != null && !webhookProperties.getAllowedUpdates().isEmpty()) {
            setWebhook.allowedUpdates(webhookProperties.getAllowedUpdates().toArray(String[]::new));
        }

        if (StringUtils.hasText(webhookProperties.getIdAddress())) {
            setWebhook.ipAddress(webhookProperties.getIdAddress());
        }

        if (StringUtils.hasText(webhookProperties.getSecretToken())) {
            setWebhook.secretToken(webhookProperties.getSecretToken());
        }

        if (StringUtils.hasText(webhookProperties.getCertificatePath())) {
            Path path = Paths.get(webhookProperties.getCertificatePath());
            if (!Files.exists(path)) {
                throw new IllegalStateException(
                        "Certificate file does not exist: '" + webhookProperties.getCertificatePath() + "'");
            } else if (!Files.isRegularFile(path)) {
                throw new IllegalStateException(
                        "Certificate path is not a regular file: '" + webhookProperties.getCertificatePath() + "'");
            } else {
                File certFile = path.toFile();
                setWebhook.certificate(certFile);
            }
        }

        return setWebhook;
    }

    private DeleteWebhook createDeleteWebhookRequest() {
        DeleteWebhook deleteWebhook = new DeleteWebhook();
        deleteWebhook.dropPendingUpdates(webhookProperties.getDropPendingUpdatesOnRemove());
        return deleteWebhook;
    }
}
