# Advanced Webhook Bot Example

This example demonstrates a more advanced usage of **telegram-bot-spring**, using:

- Webhook mode  
- A `/start` command handler  
- A catch-all text message handler  
- Proper routing using rules  
- Spring Boot auto-configuration  

---
## 📦 Project Structure

```
advanced-webhook-bot/
├── src/main/java/com/example/bot/
│   ├── StartCommandHandler.java
│   ├── AnyMessageHandler.java
│   ├── StartCommandRule.java
│   └── AnyMessageRule.java
└── src/main/resources/
    └── application.yaml
```


## ⚙️ 1. Configure Webhook Mode

`src/main/resources/application.yaml`

```yaml
telegram:
  bot:
    token: ${BOT_TOKEN}
    mode: WEBHOOK
    webhook:
      external-url: https://your-domain.com/webhook
      path: /webhook
```

> Make sure your domain is **HTTPS**.


## 🧑‍💻 2. /start Command Handler
StartCommandHandler.java

```java
@Component
public class StartCommandHandler implements CommandUpdateHandler {
    private final TelegramBotExecutor executor;

    public StartCommandHandler(TelegramBotExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        executor.execute(new SendMessage(chatId, "👋 Welcome! This is an advanced webhook bot."));
    }

    @Override
    public Set<String> commands() {
        return Set.of("/start");
    }
}
```
## 📌 3. Rule for /start Command
StartCommandRule.java

```java
@Component
public class StartCommandRule implements CommandRule {
    private final StartCommandHandler handler;

    public StartCommandRule(StartCommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public UpdateHandler updateHandler() {
        return handler;
    }
}
```
## 💬 4. Catch-All Message Handler  
`AnyMessageHandler.java`

```java
@Component
public class AnyMessageHandler implements MessageUpdateHandler {
    private final TelegramBotExecutor executor;

    public AnyMessageHandler(TelegramBotExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.message().chat().id();
        String text = update.message().text();

        executor.execute(new SendMessage(chatId, "You said: " + text));
    }
}
```

## 🔀 5. Rule for Message Handling


```java

@Component
public class AnyMessageRule implements MessageRule {
    private final AnyMessageHandler handler;

    public AnyMessageRule(AnyMessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public Matcher<Message> matcher() {
        return msg -> true; // match all messages
    }

    @Override
    public UpdateHandler updateHandler() {
        return handler;
    }
}
```
## ▶️ Run the Bot
```bash

mvn spring-boot:run
```
Your advanced webhook bot is now live!
