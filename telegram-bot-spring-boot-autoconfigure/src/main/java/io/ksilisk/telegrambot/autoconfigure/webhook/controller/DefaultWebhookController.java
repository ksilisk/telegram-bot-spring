package io.ksilisk.telegrambot.autoconfigure.webhook.controller;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.delivery.UpdateDelivery;
import io.ksilisk.telegrambot.core.webhook.WebhookController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "${telegram.bot.webhook.endpoint:/telegrambot/webhook}")
public class DefaultWebhookController implements WebhookController {
    private final UpdateDelivery updateDelivery;

    public DefaultWebhookController(UpdateDelivery updateDelivery) {
        this.updateDelivery = updateDelivery;
    }

    @PostMapping
    public String webhook(@RequestBody Update update) {
        try {
            updateDelivery.deliver(List.of(update));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "ok";
    }
}
