# Advanced Long-Polling Example

This example demonstrates **advanced usage** of the **Telegram Bot Spring Boot Starter** in **Long Polling** mode.

It focuses on:

- Chaining and ordering of **interceptors**

- Multiple **exception handlers** with custom selection

- Multiple **“no match” strategies** with custom selection

- Rule-based message routing with **priorities** and defaults

- Interaction between handlers, rules, and strategies


---

## Getting Started

### 1. Configure your bot token

Set your token in src/main/resources/application.yaml:

```yaml
spring:
  application:
    name: telegram-bot-advanced-sample-long-polling

telegram:
  bot:
    token: "your-bot-token-here"
```

Long polling mode is used by default.

### 2. Run the application

```bash
mvn spring-boot:run
```

The bot will start polling Telegram for updates.

---

## Project Structure

```
examples/advanced/longpolling
 ├─ TelegramBotAdvancedSampleLongPollingApplication.java   # Application entry point
 ├─ config/
 │   └─ TelegramBotConfig.java                             # Custom selectors for exceptions & no-match strategies
 ├─ handler/
 │   ├─ command/                                           # Command handlers
 │   ├─ callback/                                          # Callback handlers
 │   ├─ message/                                           # Message handlers
 │   └─ exception/                                         # Exception handlers
 ├─ interceptor/                                           # Update interceptors
 ├─ nomatch/                                               # No-match strategies
 ├─ rule/
 │   └─ message/                                           # Message routing rules
 └─ resources/
     └─ application.yaml                                   # Bot configuration
```

---

## Commands & Behavior

### /start, /help

Handled by StartHelpCommandUpdateHandler:

- Sends a multi-line help message:

    - /throw — throws an exception in a command handler

    - /help — shows help again

    - /callback — sends a message with callback buttons

    - /some — will be processed by a _no-match_ strategy

    - Plain messages containing "test" will be processed by a special handler

### **/callback**

Handled by CallbackCommandUpdateHandler:

- Sends a message with two inline buttons:

    - click_me — has a corresponding callback handler

    - throw_exception — calls a handler that throws an exception

Callback handlers:

- ClickMeCallbackUpdateHandler — replies with success message to click_me

- ThrowExceptionCallbackUpdateHandler — throws an exception for throw_exception


### /throw

Handled by ThrowCommandUpdateHandler:

- Always throws IllegalStateException("Some exception") to demonstrate error handling.

---

## Message Routing Rules

Located in rule/message:

### TestMessageUpdateRule

- Matches messages where text contains "test" (case-insensitive).

- Uses TestMessageUpdateHandler to reply with:

  > Your ‘test’ message was successfully handled

- Has **higher priority** via getOrder():


```java
@Override
public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE + 1;
}
```

### **DefaultMessageUpdateRule**

- Marked with @Order(Ordered.LOWEST_PRECEDENCE).

- Matches **any** message (matcher() -> true).

- Uses DefaultMessageUpdateHandler to reply with:

  > This message isn’t supported

Effectively:

- Messages containing "test" → handled by TestMessageUpdateHandler

- All other messages → handled by DefaultMessageUpdateHandler

  (unless a more specific handler/rule matches first)

---

## Interceptors

Defined in interceptor package.

### FilterMessageReactionUpdateInterceptor

- Checks if the update contains a messageReaction.

- If yes:

    - Logs the update

    - Returns null to **stop further processing** of this update.


- If not:

    - Returns the update unchanged, allowing subsequent interceptors and handlers to run.

This shows how to **filter out specific update types** before they reach the routing pipeline.

### LoggingUpdateInterceptor

- Logs each update:

```
log.info("Handled update: {}", input);
```

- Always returns the update (does not block processing).


Together they form a chain:

1. Filter out unwanted updates (message reactions).

2. Log all processed updates.


---

## Exception Handling

Defined in handler/exception:

### **LoggingUpdateExceptionHandler**

- Implements UpdateExceptionHandler.

- supports() always returns true → can handle any exception.

- Logs the exception and the update with warn level.

- getOrder() returns Ordered.HIGHEST_PRECEDENCE → very high priority.


### UserMessageUpdateExceptionHandler

- Handles exceptions **only for message-based updates** (supports() checks update.message() != null).

- Sends the fallback message to the user:

  > Internal bot exception.

- terminal() returns true → stops further exception handlers once it runs.

- Annotated with @Order(Ordered.LOWEST_PRECEDENCE) but also:

    - Implements getOrder() indirectly via base interface defaults & selector logic

    - Used here mainly to demonstrate behavior of combined @Order and getOrder() together with selectors.

### Exception Handler Selection

In TelegramBotConfig:

```java
@Bean
public UpdateExceptionHandlerSelector updateExceptionHandlerSelector() {
    return (exceptionHandlers, t, update) -> exceptionHandlers.subList(0, 2);
}
```

This selector:

- Receives the full ordered list of UpdateExceptionHandlers

- Selects **only the first two** for actual processing


This demonstrates how you can:

- Override the default selection strategy

- Limit the number of handlers used

- Dynamically choose handlers based on exception type and update


---

## No-Match Strategies

Defined in nomatch package.

These strategies are used when **no handler/rule matches the incoming update**.

### LoggingUpdateNoMatchStrategy

- Logs a warning:


```
log.warn("No handler is found for update: {}", update);
```

- Annotated with @Order(Ordered.HIGHEST_PRECEDENCE + 1).


### UserMessageUpdateNoMatchStrategy

- Sends a fallback message to the user:

  > Not found a handler to process this update

- terminal() returns true → stops further “no match” strategies.

- Annotated with @Order(Ordered.HIGHEST_PRECEDENCE) _and_ overrides getOrder() with Ordered.LOWEST_PRECEDENCE, illustrating that:

    - getOrder() has higher precedence than @Order in ordering.

- Marked as @Component.


### No-Match Strategy Selection

Also in TelegramBotConfig:

```java
@Bean
public UpdateNoMatchStrategySelector updateNoMatchStrategySelector() {
    return (noMatchStrategies, update) -> noMatchStrategies.subList(0, 2);
}
```

This selector:

- Receives the full ordered list of UpdateNoMatchStrategies

- Selects **only the first two** for actual processing

Combined with handler logic, this shows how to:

- Log that no handler was found

- Optionally send a user-facing fallback message

- Control how many strategies are executed


---

## Summary

This advanced example shows how to:

- Use **long polling** mode with the starter

- Route updates via **rules** with explicit ordering

- Build chains of **interceptors** to preprocess or filter updates

- Configure multiple **exception handlers** and control which ones run

- Configure multiple **no-match strategies** and control fallbacks

- Customize selection logic via UpdateExceptionHandlerSelector and UpdateNoMatchStrategySelector




Use this module as a reference when designing more complex bots with:

- Multiple error handling layers

- Sophisticated routing and fallback behavior

- Selective processing of specific update types (e.g. filtering out reactions)
