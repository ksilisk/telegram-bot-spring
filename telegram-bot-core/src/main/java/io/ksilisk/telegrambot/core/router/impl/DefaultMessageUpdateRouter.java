package io.ksilisk.telegrambot.core.router.impl;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.registry.handler.command.CommandHandlerRegistry;
import io.ksilisk.telegrambot.core.registry.rule.message.MessageRuleRegistry;
import io.ksilisk.telegrambot.core.router.MessageUpdateRouter;
import io.ksilisk.telegrambot.core.router.detector.CommandDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DefaultMessageUpdateRouter implements MessageUpdateRouter {
    private static final Logger log = LoggerFactory.getLogger(DefaultMessageUpdateRouter.class);

    private final CommandHandlerRegistry commandHandlerRegistry;
    private final MessageRuleRegistry messageRuleRegistry;
    private final CommandDetector commandDetector;

    public DefaultMessageUpdateRouter(CommandHandlerRegistry commandHandlerRegistry,
                                      MessageRuleRegistry messageRuleRegistry,
                                      CommandDetector commandDetector) {
        this.commandHandlerRegistry = commandHandlerRegistry;
        this.messageRuleRegistry = messageRuleRegistry;
        this.commandDetector = commandDetector;
    }

    @Override
    public boolean supports(Update update) {
        return update.message() != null;
    }

    @Override
    public boolean route(Update update) {
        if (!this.supports(update)) {
            log.warn("MessageUpdateRouter invoked with unsupported update (missing message). \n" +
                    "Ensure that router.supports(update) is checked before calling route().");
            return false;
        }

        Message message = update.message();
        String text = Optional.ofNullable(message.text()).orElse("");

        Optional<String> commandOpt = commandDetector.detectCommand(text);
        if (commandOpt.isEmpty()) {
            return routeMessage(update);
        }

        return routeCommand(update, commandOpt.get());
    }

    private boolean routeMessage(Update update) {
        Optional<UpdateHandler> updateHandler = messageRuleRegistry.find(update.message());
        if (updateHandler.isEmpty()) {
            return false;
        }
        updateHandler.get().handle(update);
        return true;

    }

    private boolean routeCommand(Update update, String command) {
        Optional<UpdateHandler> updateHandler = commandHandlerRegistry.find(command);
        if (updateHandler.isEmpty()) {
            return false;
        }
        updateHandler.get().handle(update);
        return true;
    }
}
