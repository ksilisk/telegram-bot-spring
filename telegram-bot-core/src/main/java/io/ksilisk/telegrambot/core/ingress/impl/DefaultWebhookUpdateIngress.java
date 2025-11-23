package io.ksilisk.telegrambot.core.ingress.impl;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.delivery.UpdateDelivery;
import io.ksilisk.telegrambot.core.ingress.WebhookUpdateIngress;

import java.util.List;

public class DefaultWebhookUpdateIngress implements WebhookUpdateIngress {
    private final UpdateDelivery updateDelivery;

    public DefaultWebhookUpdateIngress(UpdateDelivery updateDelivery) {
        this.updateDelivery = updateDelivery;
    }

    @Override
    public void handle(Update update) {
        updateDelivery.deliver(List.of(update));
    }
}
