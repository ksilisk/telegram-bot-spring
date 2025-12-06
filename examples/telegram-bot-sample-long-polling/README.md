# Long-Polling Example

This example demonstrates how to run a Telegram Bot using **Long Polling** with the **Telegram Bot Spring Boot Starter**.

It shows how to:

- Register command, message, callback, and inline handlers
- Use rules-based update routing
- Add update interceptors
- Execute Telegram API requests through `TelegramBotExecutor`

---

## Getting Started

### 1. Configure your bot token

Set your token in `src/main/resources/application.yaml`:

```yaml
telegram:
  bot:
    token: "your-bot-token-here"
```

### 2. Run the application

```bash
mvn spring-boot:run
```

The bot will start polling Telegram for updates.

## Project Structure

```
examples/longpolling
 ├─ LongPollingSampleApplication.java   # Application entry point
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
     └─ application.yaml                # Bot configuration
```

## Included Handlers

### Commands

- /start, /help — replies with a simple greeting

- /callback — sends a message with inline buttons

- /throw, /exception — throws an example exception (for testing)

### Messages

- AnyMessageHandler — handles any incoming text message

### Callback Queries

- TestCallbackHandler — processes callback queries with data "test"

### Inline Queries

- AnyInlineHandler — returns a simple inline article in response to any query

## Routing Rules

The example includes rule-based routing:

- AnyMessageUpdateRule — matches all messages

- AnyInlineUpdateRule — matches all inline queries

Rules define which handler should process a specific type of update.

## Update Interceptor

LoggingUpdateInterceptor logs each received Update before it is passed further through the processing pipeline.
