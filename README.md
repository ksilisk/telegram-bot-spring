# **Telegram Bot Spring Boot Starter**

A modular Spring Boot framework for building Telegram bots on **Java 17** and Spring Boot ecosystem.

This project provides a structured, extensible foundation for Telegram bots that require clear routing, lifecycle management, transport abstraction, and optional observability.

---

## **Overview**

The framework is designed around a **transport-agnostic core pipeline** with explicit extension points.

It allows building anything from small bots to larger, long-lived applications without rewriting the architecture.

The project follows standard Spring Boot conventions and avoids hidden runtime magic.

---

## **Key characteristics**

- Modular, layered architecture
- Transport-independent core
- Explicit update processing pipeline
- Multiple delivery modes (long polling, webhook)
- Pluggable HTTP client implementations
- Optional metrics and observability
- Spring Boot auto-configuration
- Runnable example applications

---

## **Quick start**

### **1. Add dependency**

```xml
<dependency>
    <groupId>io.github.ksilisk</groupId>
    <artifactId>telegram-bot-spring-boot-starter</artifactId>
    <version>0.3.0</version>
</dependency>
```

By default, this enables **long polling** transport.

---

### **2. Configure bot token**

```yaml
telegram:
  bot:
    token: ${TELEGRAM_BOT_TOKEN}
```

---

### **3. Implement a handler**

```java
@Component
public class StartCommandHandler implements CommandUpdateHandler {
    private final TelegramBotExecutor executor;

    public StartCommandHandler(TelegramBotExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void handle(Update update) {
        executor.execute(
            new SendMessage(update.message().chat().id(), "Hello")
        );
    }

    @Override
    public Set<String> commands() {
        return Set.of("/start");
    }
}
```

Run the Spring Boot application to start the bot.

---

## **Processing model**

Conceptually, updates are processed as follows:

```
Telegram
  ↓
Ingress (long polling / webhook)
  ↓
Delivery (thread pool)
  ↓
Interceptors
  ↓
Dispatcher
  ↓
Routers
  ↓
Handlers
  ↓
(No-match strategies / Exception handlers)
```

Application code usually interacts only with **handlers**, **rules**, and optional interceptors.

---

## **Project structure**

The repository is organized into focused modules:

|**Module**|**Description**|
|---|---|
|telegram-bot-core|Core processing pipeline, routing, handlers, SPI|
|telegram-bot-long-polling|Long polling transport (default)|
|telegram-bot-webhook|Webhook transport (opt-in)|
|telegram-bot-observability|Metrics and observability integration|
|telegram-bot-spring-boot-autoconfigure|Spring Boot auto-configuration|
|telegram-bot-spring-boot-starter|Starter dependency|
|telegram-bot-dependencies|BOM for dependency management|
|examples/|Runnable sample applications|

Each module contains a dedicated README with detailed documentation.

---

## **Transport selection**

The framework supports multiple update ingestion modes.

### **Built-in transports**

- **LONG_POLLING** (default)

  Receives updates via Telegram long polling. Requires no HTTP endpoint.

- **WEBHOOK**

  Receives updates via HTTPS callbacks from Telegram.

  Requires an explicit dependency and a public HTTPS endpoint.

### **Custom ingress**

- **CUSTOM**

  Disables built-in transports and allows applications to provide their own UpdateIngress implementation.


This mode is intended for advanced use cases, such as:

- custom gateways or proxies
- message queues or event streams
- testing and replaying updates
- non-standard Telegram delivery mechanisms

Transport selection is controlled via:

```yaml
telegram:
  bot:
    mode: LONG_POLLING | WEBHOOK | CUSTOM
```

In CUSTOM mode, application startup will fail if no UpdateIngress bean is provided.

---

## **Configuration model**

Configuration is based on Spring Boot @ConfigurationProperties and follows a single, consistent prefix:

```
telegram.bot.*
```

Transport-specific and optional modules introduce their own nested namespaces.

---

## **Examples**

The [/examples](examples) directory contains standalone Spring Boot applications demonstrating:

- minimal long polling setup
- webhook configuration and lifecycle
- routing and handler composition

Each example can be run independently.

---

## **Design principles**

- Explicit over implicit
- Interfaces over implementations
- Transport-agnostic core
- Predictable lifecycle and threading
- Production-oriented defaults

---

## **Documentation entry points**

- **Core architecture**: [telegram-bot-core/README.md](telegram-bot-core/README.md)
- **Long polling transport**: [telegram-bot-long-polling/README.md](telegram-bot-long-polling/README.md)
- **Webhook transport**: [telegram-bot-webhook/README.md](telegram-bot-webhook/README.md)
- **Observability**: [telegram-bot-observability/README.md](telegram-bot-observability/README.md)

---
