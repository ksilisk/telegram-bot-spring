# Webhook Example

This example demonstrates how to run a Telegram Bot using **Webhook mode** with the **Telegram Bot Spring Boot Starter**.

It shows how to:

- Register command, message, callback, and inline handlers

- Use rules-based update routing

- Add update interceptors

- Execute Telegram API requests through TelegramBotExecutor

- Configure and expose a webhook endpoint

---

## Getting Started

### 1. Configure your bot token and webhook settings

Set your configuration in src/main/resources/application.yaml:

```yaml
telegram:
  bot:
    mode: webhook
    webhook:
      auto-register: false
      auto-remove: false
    token: "your-token-is-here"

server:
  port: 8181
```

> By default, the webhook endpoint will be exposed at:

> **POST http://localhost:8181/telegrambot/webhook**

You can enable automatic webhook registration by setting:

```
telegram.bot.webhook.auto-register: true
```

---

## Running the Application

```bash
mvn spring-boot:run
```

After starting, configure your bot’s webhook (manually or automatically) to point to your server:

```
https://your-domain/telegram/update
```

---

## **Project Structure**

```
examples/webhook
 ├─ WebhookSampleApplication.java       # Application entry point
 ├─ handler/
 │   ├─ command/                        # Command handlers
 │   ├─ message/                        # Message handler
 │   ├─ inline/                         # Inline handler
 │   └─ callback/                       # Callback handler
 ├─ interceptor/
 │   └─ LoggingUpdateInterceptor.java   # Logs incoming updates
 ├─ rule/
 │   ├─ AnyMessageUpdateRule.java       # Matches all messages
 │   └─ AnyInlineUpdateRule.java        # Matches all inline queries
 └─ resources/
     └─ application.yaml                # Bot webhook configuration
```

---

## Included Handlers

### Commands

- /start, /help — reply with a greeting

- /callback — sends a message with inline buttons

- /throw, /exception — throw an example exception (for testing)


### Messages

- AnyMessageHandler — handles any incoming text

### Callback Queries

- TestCallbackHandler — responds to callback "test"

### Inline Queries

- AnyInlineHandler — returns a simple inline article result

---

## Routing Rules

The example demonstrates rule-based routing:

- AnyMessageUpdateRule — matches all text messages
- AnyInlineUpdateRule — matches all inline queries

Each rule returns a specific handler responsible for processing the update.

---

## Update Interceptor

LoggingUpdateInterceptor logs every incoming Update before routing and handling.

---
