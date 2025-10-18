package io.ksilisk.telegrambot.core.router;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.registry.handler.command.CommandHandlerRegistry;
import io.ksilisk.telegrambot.core.registry.rule.message.MessageRuleRegistry;

import java.util.Optional;

public class MessageUpdateRouter implements UpdateRouter {
    private final CommandHandlerRegistry commandHandlerRegistry;
    private final MessageRuleRegistry messageRuleRegistry;

    public MessageUpdateRouter(CommandHandlerRegistry commandHandlerRegistry,
                               MessageRuleRegistry messageRuleRegistry) {
        this.commandHandlerRegistry = commandHandlerRegistry;
        this.messageRuleRegistry = messageRuleRegistry;
    }

    @Override
    public boolean supports(Update update) {
        return update.message() != null;
    }

    @Override
    public boolean route(Update update) {
        if (isCommand(update.message())) {
            return routeCommand(update);
        } else {
            return routeMessage(update);
        }
    }

    private boolean routeMessage(Update update) {
        Optional<UpdateHandler> updateHandler = messageRuleRegistry.find(update.message());
        if (updateHandler.isEmpty()) {
            return false;
        }
        updateHandler.get().handle(update);
        return true;

    }

    private boolean routeCommand(Update update) {
        String command = update.message().text().split(" ")[0];
        Optional<UpdateHandler> updateHandler = commandHandlerRegistry.find(command);
        if (updateHandler.isEmpty()) {
            return false;
        }
        updateHandler.get().handle(update);
        return true;
    }

    private boolean isCommand(Message message) {
        return message.text() != null && message.text().startsWith("/");
    }
}
