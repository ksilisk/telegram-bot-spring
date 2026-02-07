# Telegram Bot Observability

`telegram-bot-observability` is an observability module for **Telegram Bot Spring Boot Starter**.
It provides **out-of-the-box metrics** for update processing, handlers, Telegram API calls, and delivery thread pools — without requiring any changes to business logic.

The module is designed for **Spring Boot 3.x** and integrates with **Micrometer**.

---

## Features

The module automatically collects metrics for:

- incoming updates (delivery and batching)
- routing and update handlers
- Telegram API calls
- update delivery thread pools
- exceptions and unmatched routing

All metrics are collected **transparently** using Spring `BeanPostProcessor` and `MethodInterceptor`.

---

## Getting Started

### Dependency

Add the observability module to your project:

```xml
<dependency>
    <groupId>io.github.ksilisk</groupId>
    <artifactId>telegram-bot-observability</artifactId>
</dependency>
```

To export metrics, add a Micrometer backend, for example, Prometheus:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

---

## **How It Works**

The module relies on the following mechanisms:
- **BeanPostProcessor** — wraps core Telegram bot components
- **MethodInterceptor** — records metrics around method invocations
- **MetricsRecorder** — abstraction over the underlying metrics backend

If no MeterRegistry is present in the Spring context, observability is **automatically disabled** using a NoopMetricsRecorder.

---

## **Collected Metrics**

### **Updates**

|**Metric**|**Type**|**Description**|
|---|---|---|
|telegram.bot.updates.received|Counter|Total number of received updates|
|telegram.bot.updates.batch_size|DistributionSummary|Size of update batches passed to delivery|

---

### **Routing**

|**Metric**|**Type**|**Description**|
|---|---|---|
|telegram.bot.routing.no_match|Counter|Updates that did not match any handler rule|

---

### **Handlers**

|**Metric**|**Type**|**Tags**|
|---|---|---|
|telegram.bot.handler.invocations|Counter|channel, handler, status|
|telegram.bot.handler.duration|Timer|channel, handler, status|

**Channels** represent logical handler categories (e.g. command, message, callback) and are resolved via TelegramBotChannelResolver.

---

### **Telegram API**

|**Metric**|**Type**|**Tags**|
|---|---|---|
|telegram.bot.api.calls|Counter|method, status|
|telegram.bot.api.duration|Timer|method, status|

---

### **Exceptions**

|**Metric**|**Type**|**Tags**|
|---|---|---|
|telegram.bot.exceptions.total|Counter|exception|

---

### **Delivery Thread Pool**

|**Metric**|**Type**|**Description**|
|---|---|---|
|telegram.bot.delivery.pool.size|Gauge|Current thread pool size|
|telegram.bot.delivery.pool.active|Gauge|Number of active threads|
|telegram.bot.delivery.pool.queue.size|Gauge|Size of the delivery queue|

---

### **Telegram File API**


|**Metric**|**Type**|**Description**|
|---|---|---|
|telegram.bot.file.download.duration|Timer|Time spent downloading a file into memory via TelegramBotFileClient.downloadBy*(..)|
|telegram.bot.file.stream.open.duration|Timer|Time spent opening a download stream via TelegramBotFileClient.openStreamBy*(..)|
|telegram.bot.file.calls|Counter|Total number of TelegramBotFileClient operations|
|telegram.bot.file.download.bytes|DistributionSummary|Number of bytes downloaded by TelegramBotFileClient downloadBy* methods|

## **Customization**

### **Channel Resolution**

You can customize how handler channels are resolved by providing your own TelegramBotChannelResolver:

```java
@Bean
public TelegramBotChannelResolver customChannelResolver() {
    return handler -> TelegramBotChannel.OTHER;
}
```

---

### **Metrics Backend**

The module provides a default Micrometer-based implementation:
- MicrometerMetricsRecorder
- NoopMetricsRecorder (fallback)

You may replace it with a custom implementation by defining your own MetricsRecorder bean.

---

## **Limitations**

- Final classes or final methods cannot be instrumented and will be skipped
- Metrics collection relies on Spring proxying (AOP-based)

---

## **Design Goals**
- Zero configuration by default
- Low-cardinality metric tags
- No impact on business logic
- Safe fallback when metrics are disabled
