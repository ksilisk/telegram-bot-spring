# Telegram Bot Advanced Example

This is an advanced example demonstrating the full capabilities of the Telegram Bot Spring Boot Starter.

## Features Demonstrated

This example showcases:

1. **Multiple Handler Types**:
   - Command handlers (`/start`, `/help`, `/stats`)
   - Callback query handlers for inline buttons
   - Text message handlers with custom routing rules
   - Photo and document handlers

2. **Interceptors**:
   - Logging interceptor for debugging
   - Metrics interceptor for tracking bot usage
   - Authorization interceptor for admin commands

3. **Advanced Routing**:
   - Pattern-based message routing
   - Conditional routing based on user state
   - Fallback handlers for unknown commands

4. **State Management**:
   - In-memory user session management
   - Conversation flow handling

5. **Inline Keyboards**:
   - Dynamic inline keyboard generation
   - Callback data handling
   - Multi-step interactions

## Architecture

```
src/main/java/
└── io.ksilisk.example.advanced/
    ├── TelegramBotAdvancedApplication.java
    ├── config/
    │   └── BotConfiguration.java
    ├── handler/
    │   ├── command/
    │   │   ├── StartCommandHandler.java
    │   │   ├── HelpCommandHandler.java
    │   │   └── StatsCommandHandler.java
    │   ├── callback/
    │   │   └── MenuCallbackHandler.java
    │   ├── message/
    │   │   ├── EchoMessageHandler.java
    │   │   └── PhotoMessageHandler.java
    │   └── fallback/
    │       └── UnknownCommandHandler.java
    ├── interceptor/
    │   ├── LoggingInterceptor.java
    │   ├── MetricsInterceptor.java
    │   └── AuthorizationInterceptor.java
    ├── service/
    │   ├── UserSessionService.java
    │   └── StatisticsService.java
    └── model/
        └── UserSession.java
```

## Running the Example

1. **Set your bot token**:
   ```properties
   telegram.bot.token=YOUR_BOT_TOKEN_HERE
   ```

2. **Build and run**:
   ```bash
   cd examples/telegram-bot-sample-advanced
   mvn spring-boot:run
   ```

3. **Test the bot** by sending these commands:
   - `/start` - Initialize bot and see welcome message with inline keyboard
   - `/help` - Display available commands
   - `/stats` - View bot usage statistics
   - Send any text message to see echo functionality
   - Send a photo to see photo handling

## Key Concepts

### Multiple Handlers
The bot handles different types of updates through specialized handlers, making the code modular and maintainable.

### Interceptor Chain
Interceptors process updates before they reach handlers, enabling cross-cutting concerns like logging, authentication, and metrics.

### State Management
The `UserSessionService` maintains user state across interactions, enabling complex conversation flows.

### Inline Keyboards
Demonstrates building interactive menus with callback buttons and handling user selections.

## Learning Path

If you're new to this starter:
1. Start with the simple `telegram-bot-sample-long-polling` example
2. Review this advanced example to understand complex scenarios
3. Build your own bot using patterns from both examples

## References

- [Telegram Bot API Documentation](https://core.telegram.org/bots/api)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Project Main README](../../README.md)
