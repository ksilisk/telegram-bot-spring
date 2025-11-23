# Telegram Bot Spring Boot Starter

> A lightweight, developer-friendly Spring Boot starter for building Telegram bots using Java 17 and Spring Boot 3.
>
> Supports long polling, webhooks, routing, interceptors, and pluggable HTTP clients.

## 🚀 Features

* **Plug & Play** — launch a Telegram bot easily.
* **Long Polling & Webhook** — two modes out of the box.
* **Smart Routing** — commands, text messages, update types.
* **Interceptors** — logging, filtering, metrics.
* **Flexible HTTP Client** — OkHttp or Spring RestClient.
* **Auto Configuration** — minimal boilerplate.
* **Extensible Architecture** — customize any component.
* **Examples Included** — ready-to-run sample bots.

---

## ⚡ Quick Start

### 1. Add Dependency

```xml
<dependency>
    <groupId>io.ksilisk.telegrambot</groupId>
    <artifactId>telegram-bot-spring-boot-starter</artifactId>
    <version>YOUR_VERSION</version>
</dependency>
```

### 2. Configure Token

```yaml
telegram:
    bot:
        token: ${BOT_TOKEN}
```

### 3. Create Your First Handler

```java
@Component
public class StartCommandHandler implements CommandUpdateHandler {
    private final TelegramBotExecutor telegramBotExecutor;

    public StartCommandHandler(TelegramBotExecutor telegramBotExecutor) {
        this.telegramBotExecutor = telegramBotExecutor;
    }

    @Override
    public void handle(Update update) {
        SendMessage sendMessage = new SendMessage(update.message().from().id(), "Simple Hello!");
        telegramBotExecutor.execute(sendMessage);
    }

    @Override
    public Set<String> commands() {
        return Set.of("/start", "/help");
    }
}
```

### 4. Run the Application

That’s it — your bot is live ✨

---

## 🧠 How It Works (in 20 seconds)

```
Telegram → Ingress → Delivery -> Interceptors → Dispatcher → Router -> Your Handlers
```

1. Update arrives (long polling or webhook)
2. Passes through optional interceptors
3. Dispatcher selects the right handler
4. Fallback strategies handle no-match and exceptions

You write only **handlers**.

---

## ⚙️ Configuration

### Main Properties

| Property                             | Description                           |
| ------------------------------------ | ------------------------------------- |
| `telegram.bot.token`                 | Bot token (required)                  |
| `telegram.bot.mode`                  | `LONG_POLLING` (default) or `WEBHOOK` |
| `telegram.bot.client.implementation` | `AUTO` / `OKHTTP` / `SPRING`          |

### Notes

* **Webhook** mode requires `spring-boot-starter-web`.
* **AUTO** prefers OkHttp if present → otherwise uses Spring `RestClient`.

---

## 🧭 Handlers & Routing

### Callback Handler

```java
@Component
public class TestCallbackHandler implements CallbackUpdateHandler {
    private final TelegramBotExecutor telegramBotExecutor;

    public TestCallbackHandler(TelegramBotExecutor telegramBotExecutor) {
        this.telegramBotExecutor = telegramBotExecutor;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.callbackQuery().maybeInaccessibleMessage().chat().id();
        SendMessage sendMessage = new SendMessage(chatId, "Successfully handled callback 'test'");
        telegramBotExecutor.execute(sendMessage);
    }

    @Override
    public Set<String> callbacks() {
        return Set.of("test");
    }
}
```

### Catch-all Text Handler

```java
@Component
public class AnyMessageHandler implements MessageUpdateHandler {
    private final TelegramBotExecutor telegramBotExecutor;

    public AnyMessageHandler(TelegramBotExecutor telegramBotExecutor) {
        this.telegramBotExecutor = telegramBotExecutor;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        SendMessage sendMessage = new SendMessage(chatId, "Your message is successfully handled!");
        telegramBotExecutor.execute(sendMessage);
    }
}

@Component
public class AnyMessageRule implements MessageRule {
    private final AnyMessageHandler anyMessageHandler;

    public AnyMessageRule(AnyMessageHandler anyMessageHandler) {
        this.anyMessageHandler = anyMessageHandler;
    }

    @Override
    public Matcher<Message> matcher() {
        return update -> true; // handle any message, but you can add your custom rule
    }

    @Override
    public UpdateHandler updateHandler() {
        return anyMessageHandler;
    }
}

```

---

## 🔌 Interceptors

```java
@Component
public class LoggingUpdateInterceptor implements UpdateInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LoggingUpdateInterceptor.class);

    @Override
    public Update intercept(Update input) {
        log.info("Handled an update: {}", input);
        return input;
    }
}

```

* Interceptors must be **thread-safe**
* They run **before** dispatcher
* Return `null` to drop an update

---

## 🔧 Choosing HTTP Client

| Mode     | Behavior                               |
| -------- | -------------------------------------- |
| `AUTO`   | Prefer OkHttp → fallback to RestClient |
| `OKHTTP` | Fail if OkHttp not on classpath        |
| `SPRING` | Fail if Spring Web not on classpath    |

---

## 📦 Examples

Examples are located in the `/examples` directory:

* **telegram-bot-sample-long-polling** — minimal bot example

Run:

```bash
cd examples/telegram-bot-sample-long-polling
mvn spring-boot:run
```
### 📌 Additional Example: Simple Echo Bot

Here is a minimal example showing how to receive a message and reply using this starter.

#### 1. Configure your bot

Create `src/main/resources/application.properties`:

```properties
telegram.bot.token=YOUR_TELEGRAM_BOT_TOKEN
telegram.bot.username=YOUR_BOT_USERNAME
```

#### 2. Create a handler to process messages

Create:


`src/main/java/com/example/bot/EchoHandler.java`

```java

@Component
public class EchoHandler implements MessageUpdateHandler {
    private final TelegramBotExecutor executor;

    public EchoHandler(TelegramBotExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void handle(Update update) {
        if (update.message() != null && update.message().text() != null) {
            Long chatId = update.message().chat().id();
            String text = update.message().text();

            SendMessage reply = new SendMessage(chatId, "You said: " + text);
            executor.execute(reply);
        }
    }
}

```

#### 3. Create a rule to route all messages

Create:

`src/main/java/com/example/bot/EchoRule.java`

```java
@Component
public class EchoRule implements MessageRule {
    private final EchoHandler handler;

    public EchoRule(EchoHandler handler) {
        this.handler = handler;
    }

    @Override
    public Matcher<Message> matcher() {
        return msg -> true; // handle every message
    }

    @Override
    public UpdateHandler updateHandler() {
        return handler;
    }
}

```

#### 4. Run the bot
```bash
mvn spring-boot:run
```

Now the bot will reply:

```
You said: <the user’s message>
```


---

## 🤝 Contributing

PRs are welcome!
See `CONTRIBUTING.md` for details.

---

## 🔐 Security

Security guidelines are available in `SECURITY.md`.

---

## 📄 License

MIT — see `LICENSE` file.
