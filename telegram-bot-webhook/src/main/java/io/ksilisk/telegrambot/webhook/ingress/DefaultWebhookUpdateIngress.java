package io.ksilisk.telegrambot.webhook.ingress;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.delivery.UpdateDelivery;
import io.ksilisk.telegrambot.core.update.Updates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DefaultWebhookUpdateIngress implements WebhookUpdateIngress {
    private static final Logger log = LoggerFactory.getLogger(DefaultWebhookUpdateIngress.class);

    private final UpdateDelivery updateDelivery;

    public DefaultWebhookUpdateIngress(UpdateDelivery updateDelivery) {
        this.updateDelivery = updateDelivery;
    }

    @Override
    public void handle(Update update) {
        log.debug("Received {} update with id={} via webhook ingress", Updates.type(update), update.updateId());
        updateDelivery.deliver(List.of(update));
    }
}
