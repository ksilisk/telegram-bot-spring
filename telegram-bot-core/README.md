# **telegram-bot-core**

Core update-processing engine for **Telegram Bot Spring Boot Starter**.

This module defines the **core processing pipeline**, public **API contracts**, and **extension points** for building Telegram bots.

It is transport-agnostic and does not depend on Spring Web or any specific delivery mechanism.

---

## **Responsibilities**

telegram-bot-core is responsible for:
- defining the update processing pipeline
- routing updates to handlers
- providing extension points for interception, error handling, and fallbacks
- abstracting Telegram Bot API execution

It does **not** deal with:
- HTTP, webhooks, or long polling
- Spring Boot auto-configuration
- application startup or lifecycle management

---

## **High-level pipeline**

The core processes updates using the following conceptual pipeline:

```
Ingress
  ↓
Delivery
  ↓
Interceptors
  ↓
Dispatcher
  ↓
Routers
  ↓
Handlers
  ↓
(NoMatchStrategy / ExceptionHandler)
```

Each stage is represented by explicit interfaces and can be customized or replaced.

---

## **Key concepts**

### **Update ingress**

Ingress is an external source of updates (e.g. webhook, long polling).

Core defines marker interfaces only:
- Ingress
- UpdateIngress

Concrete ingress implementations live in transport modules.

---

### **Delivery**

Delivery is responsible for **batch submission**, **threading**, and **lifecycle**.

Main contracts:
- Delivery
- UpdateDelivery
- DeliveryThreadPoolExecutorFactory

Default implementation uses a configurable thread pool and is shared across transports.

---

### **Interceptors**

Interceptors allow observing or transforming updates **before routing**.

Contracts:
- Interceptor<\U>
- UpdateInterceptor

Typical use cases:
- logging
- tracing
- observability hooks
- lightweight preprocessing

Interceptors are ordered and composable.

---

## **MDC and logging context**

Core provides small utilities to enrich logs with Telegram update context using SLF4J **MDC**.

### **MDCSnapshot**

MDCSnapshot captures the current MDC context map and can propagate it to another thread.

Typical use case:
- preserve external MDC (e.g. request/trace ids) when delegating update processing to a thread pool

Usage:
- capture once (often per batch)
- wrap tasks before submitting them to an executor

### **UpdateMDC**

UpdateMDC populates MDC with context extracted from a Telegram Update.

Keys:
- update_id
- update_type
- user_id
- chat_id

UpdateMDC.open(update) returns an AutoCloseable scope that restores previous MDC values on close.

Use it with try-with-resources to avoid context leakage in thread pools.

---

### **Dispatching**

Dispatching is the hand-off point between delivery and routing.

Contracts:
- Dispatcher<\U>
- UpdateDispatcher

Responsibilities:
- receive a single update
- forward it to the routing layer
- ensure consistent invocation semantics

---

### **Routing**

Routing determines **which handler should process an update**.

Core provides:

- Router
- UpdateRouter
- specialized routers:
    - message
    - callback query
    - inline query
    - command

Routers typically consult registries and rules.

---

### **Registries**

Registries map updates or keys to handlers.

Key registry types:
- command handler registry
- callback handler registry
- message rule registry
- inline rule registry

Registries are responsible for **lookup**, not execution.

---

### **Handlers**

Handlers contain **user-defined business logic**.

Main contracts:
- Handler<\U>
- UpdateHandler
- specializations:
    - command handlers
    - callback handlers
    - message handlers
    - inline handlers

Handlers are invoked only after routing succeeds.

---

### **Rules and matchers**

Rules allow conditional routing based on predicates.

Core abstractions:
- Rule
- UpdateRule
- Matcher

Rules are ordered and evaluated sequentially.

---

## **Error handling and fallbacks**

### **No-match strategies**

If no handler matches an update, one or more **no-match strategies** are applied.

Contracts:
- NoMatchStrategy
- UpdateNoMatchStrategy

Strategies are ordered and may be terminal.

Typical use cases:
- logging
- metrics
- silent ignore
- fallback behavior

---

### **Exception handlers**

If an exception occurs during processing, it is delegated to exception handlers.

Contracts:
- ExceptionHandler
- UpdateExceptionHandler

Handlers can decide whether they support a given exception and whether processing should stop.

---

## **Telegram API execution**

Core abstracts Telegram API execution behind:
- ApiExecutor
- TelegramBotExecutor

This allows multiple HTTP client implementations without leaking details into handlers.

### **Retry support**

Core also provides retry abstractions for Telegram Bot API calls:
- RetryRule
- RetryDelayStrategy
- RetryingTelegramBotExecutor

Retry behavior is intentionally split into small extension points:
- rules decide whether a failed request may be retried
- delay strategies decide how long to wait before the next attempt
- the retrying executor decorates another TelegramBotExecutor

Default reusable implementations include:
- CompositeRetryRule
- MaxAttemptsRetryRule
- RetryableMethodsRetryRule
- FixedRetryDelayStrategy

### **TelegramBotFileClient**

TelegramBotFileClient provides a focused API for downloading files from Telegram via the Bot API.

Telegram file downloads are performed in two steps:
1. resolve file_id to file_path using getFile
2. download file bytes from /file/bot<token>/<file_path>

The interface typically offers two styles of methods:
- *ById(..) methods that perform both steps
- *ByPath(..) methods for cases where file_path is already known

This keeps file operations out of handlers and routing logic, while still allowing custom HTTP client implementations underneath.

---

## **Update utilities**

### **Updates**

Updates is a small utility class for working with Telegram Update objects.

It provides consistent extraction of common values across all update kinds, such as:
- update type classification
- user id extraction
- chat id extraction

The goal is to keep routing, logging, and diagnostics code simple and uniform, without duplicating Telegram object traversal logic across the pipeline.

---

## **Ordering and composition**

Many core components support ordering via:
- CoreOrdered

This allows predictable execution of:
- interceptors
- strategies
- exception handlers

---

## **Details**

Concrete default implementations may change between releases.

Users are encouraged to depend on **interfaces**, not implementations.

---

## **Intended audience**

This module is designed for:
- framework users implementing custom handlers and rules
- advanced users extending routing or delivery behavior
- contributors working on transports or integrations

If you are only building a bot, you usually interact with this module **indirectly** via the Spring Boot starter.

---
