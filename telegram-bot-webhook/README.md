# **telegram-bot-webhook**

Webhook transport module for **Telegram Bot Spring Boot Starter**.

This module provides **Telegram webhook support** for Telegram bots.

It exposes an HTTP endpoint that receives updates from Telegram and forwards them into the core update-processing pipeline.

Webhook transport is **opt-in** and must be enabled explicitly.

---

## **Overview**

telegram-bot-webhook is responsible for:
- exposing an HTTP endpoint for Telegram updates
- validating incoming webhook requests
- forwarding updates into the delivery → dispatch → routing pipeline
- managing webhook registration and removal lifecycle

It does **not** define handlers, routing rules, or business logic — those live in telegram-bot-core.

---

## **When to use**

Use **webhooks** if:
- your bot is deployed behind a **public HTTPS endpoint**
- you want **lower latency** compared to long polling
- your infrastructure supports inbound HTTP traffic (cloud, Kubernetes, reverse proxy)

If you do not want to expose an HTTP endpoint, use telegram-bot-long-polling instead.

---

## **Installation**
### **1. Add the starter**

```xml
<dependency>
    <groupId>io.github.ksilisk</groupId>
    <artifactId>telegram-bot-spring-boot-starter</artifactId>
</dependency>
```

⚠️ The starter includes **long polling only** by default.

---

### **2. Add webhook transport explicitly**

```xml
<dependency>
    <groupId>io.github.ksilisk</groupId>
    <artifactId>telegram-bot-webhook</artifactId>
</dependency>
```

---

### **3. Enable webhook mode**

```yaml
telegram:
  bot:
    mode: WEBHOOK
```

---

## **Configuration**

### **Minimal configuration**

```yaml
telegram:
  bot:
    token: ${TELEGRAM_BOT_TOKEN}
    bot-username: my_bot
    mode: WEBHOOK

    webhook:
      endpoint: /telegram/webhook # by default, endpoint is "/telegrambot/webhook"
      external-url: https://example.com
```

This registers the webhook at:

```
https://example.com/telegram/webhook
```

---

### **Webhook properties**

All webhook-related settings are configured under telegram.bot.webhook:

```yaml
telegram:
  bot:
    webhook:
      endpoint: /telegram/webhook
      external-url: https://example.com
      secret-token: my-secret
      allowed-updates:
        - message
        - callback_query
      drop-pending-updates-on-register: true
      drop-pending-updates-on-remove: true
      max-connections: 40
      auto-register: true
      auto-remove: true
```

|**Property**|**Description**|
|---|---|
|endpoint|Local HTTP endpoint path|
|external-url|Public base URL used for webhook registration|
|secret-token|Optional secret token for request validation|
|allowed-updates|Types of updates accepted by the webhook|
|drop-pending-updates-on-register|Clears pending updates on register|
|drop-pending-updates-on-remove|Clears pending updates on removal|
|max-connections|Maximum number of webhook connections|
|auto-register|Register webhook on application startup|
|auto-remove|Remove webhook on graceful shutdown|

---

## **Request validation**

If secret-token is configured:
- Incoming requests must include the header `X-Telegram-Bot-Api-Secret-Token`
- Requests with missing or invalid token are rejected before entering the pipeline

This protects the endpoint from unauthorized requests.

---

## **How it works**

At runtime, the webhook module wires the following flow:

```
Telegram API
   ↓  HTTPS POST
WebhookController
   ↓
WebhookUpdateIngress
   ↓
UpdateDelivery (thread pool)
   ↓
Interceptors
   ↓
Dispatcher
   ↓
Routers → Handlers
```

The webhook endpoint itself is stateless; all processing happens inside the core pipeline.

---

## **Webhook lifecycle**

Webhook registration is managed via the WebhookLifecycle abstraction.

By default:
- webhook is **registered on application startup**
- webhook is **removed on graceful shutdown**

Lifecycle behavior can be disabled or customized via configuration.

---

## **Auto-configuration**

Webhook support is enabled by:

```
TelegramBotWebhookAutoConfiguration
```

Activation conditions:
- telegram.bot.mode=WEBHOOK
- telegram-bot-webhook is present on the classpath
- Spring Web is available

Beans provided by this module:
- WebhookController
- WebhookUpdateIngress
- WebhookLifecycle
- WebhookProperties

---

## **Observability**

This module integrates transparently with telegram-bot-observability.

When observability is enabled:
- handler execution is measured
- errors and no-match cases are recorded

No additional webhook-specific configuration is required.

---

## **Example**

A runnable example is available in [telegram-bot-sample-webhook](../examples/telegram-bot-sample-webhook/README.md)

---
