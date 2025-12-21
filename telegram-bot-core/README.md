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
- Interceptor<U>
- UpdateInterceptor

Typical use cases:
- logging
- tracing
- observability hooks
- lightweight preprocessing

Interceptors are ordered and composable.

---

### **Dispatching**

Dispatching is the hand-off point between delivery and routing.

Contracts:
- Dispatcher<U>
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
- Handler<U>
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
