package io.ksilisk.telegrambot.webhook.controller;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.webhook.ingress.WebhookUpdateIngress;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${telegram.bot.webhook.endpoint:/telegrambot/webhook}")
public class DefaultWebhookController implements WebhookController {
    private final WebhookUpdateIngress webhookUpdateIngress;

    public DefaultWebhookController(WebhookUpdateIngress webhookUpdateIngress) {
        this.webhookUpdateIngress = webhookUpdateIngress;
    }

    @PostMapping
    public String webhook(@RequestBody Update update) {
        webhookUpdateIngress.handle(update);
        return "ok";
    }
}
