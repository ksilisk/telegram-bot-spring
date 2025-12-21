# **telegram-bot-long-polling**

Long-polling transport module for **Telegram Bot Spring Boot Starter**.

This module provides a production-ready **long polling ingress** for Telegram bots.

It integrates with the core update-processing pipeline and is automatically enabled via Spring Boot auto-configuration.

---

## **Features**
- Long polling update ingestion with lifecycle management
- Configurable polling parameters (timeout, limits, retry delay)
- Pluggable offset storage (in-memory by default)
- Seamless integration with the core update pipeline
- Fully auto-configured via Spring Boot properties

---

## **When to use**
Use **telegram-bot-long-polling** if:
- You want to run a Telegram bot **without exposing a public HTTP endpoint**
- Your bot is deployed as a standalone application (VM, container, local)
- You prefer a simpler setup compared to webhooks

If you need HTTPS endpoints and webhook delivery, consider the [telegram-bot-webhook](../telegram-bot-webhook/README.md) module instead.

---

## **Installation**
Normally, you **do not need to depend on this module directly**.
Add the starter to your project:

```xml
<dependency>
    <groupId>io.github.ksilisk</groupId>
    <artifactId>telegram-bot-spring-boot-starter</artifactId>
</dependency>
```

The long-polling module will be pulled in automatically when:
- telegram.bot.mode is LONG_POLLING (default), or
- telegram.bot.mode is not specified

---

## **Configuration**
### **Enable long polling (default)**

```yaml
telegram:
  bot:
    token: ${TELEGRAM_BOT_TOKEN}
    mode: LONG_POLLING
```

### **Long polling properties**

All long polling–specific settings are configured under:

```yaml
telegram:
  bot:
    long-polling:
      timeout: 30s
      limit: 100
      retry-delay: 1s
      allowed-updates:
        - message
        - callback_query
      drop-pending-on-start: true
      shutdown-timeout: 10s
```

|**Property**|**Description**|
|---|---|
|timeout|Long polling timeout sent to Telegram|
|limit|Maximum number of updates per request|
|retry-delay|Delay before retrying on failure|
|allowed-updates|Filter update types|
|drop-pending-on-start|Clears backlog on startup|
|shutdown-timeout|Graceful shutdown timeout|

---

## **How it works**
At runtime, the module wires the following components into the core pipeline:

```
Telegram API
   ↓
LongPollingUpdateIngress
   ↓
UpdateDelivery (thread pool)
   ↓
Interceptors
   ↓
Dispatcher
   ↓
Routers → Handlers
```

Polling lifecycle (start() / stop()) is managed automatically by Spring Boot.

---

## **Offset storage**

The module uses an **OffsetStore** abstraction to track the last processed update ID.

### **Default implementation**

- InMemoryOffsetStore

- Resets on application restart

### **Custom OffsetStore**

You can provide your own implementation (e.g. JDBC, Redis):

```java
@Bean
public OffsetStore offsetStore() {
    return new JdbcOffsetStore(dataSource);
}
```

Spring Boot will automatically pick it up.

---

## **Auto-configuration**

The module is enabled via:

```
TelegramBotLongPollingAutoConfiguration
```

Conditions:
- telegram.bot.mode=LONG_POLLING (or missing)
- Core bot configuration is present

Beans provided:
- LongPollingUpdateIngress
- UpdatePoller
- OffsetStore
- LongPollingProperties

---

## **Observability**

This module is compatible with telegram-bot-observability.

When observability is enabled:
- Polling and handler execution are automatically instrumented
- Metrics are exported via Micrometer

No additional configuration is required.

---

## **Example**

See the runnable example applications:

- [telegram-bot-sample-long-polling](../examples/telegram-bot-sample-long-polling/README.md)
- [telegram-bot-advanced-sample-long-polling](../examples/telegram-bot-advanced-sample-long-polling/README.md)

They demonstrate:
- Minimal configuration
- Command and message handlers
- Graceful shutdown

---
