package io.ksilisk.telegrambot.autoconfigure.config.registry;

import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import io.ksilisk.telegrambot.core.registry.handler.callback.CallbackHandlerRegistry;
import io.ksilisk.telegrambot.core.registry.handler.callback.DefaultCallbackHandlerRegistry;
import io.ksilisk.telegrambot.core.registry.handler.command.CommandHandlerRegistry;
import io.ksilisk.telegrambot.core.registry.handler.command.DefaultCommandHandlerRegistry;
import io.ksilisk.telegrambot.core.registry.rule.inline.DefaultInlineRuleRegistry;
import io.ksilisk.telegrambot.core.registry.rule.inline.InlineRuleRegistry;
import io.ksilisk.telegrambot.core.registry.rule.message.DefaultMessageRuleRegistry;
import io.ksilisk.telegrambot.core.registry.rule.message.MessageRuleRegistry;
import io.ksilisk.telegrambot.core.rule.InlineRule;
import io.ksilisk.telegrambot.core.rule.MessageRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class RegistryConfiguration {
    // TODO: implement a better way to add registries into Spring Context
    //  because now here is no way to customize (enabling or disabling) predefined registries
    //  https://github.com/ksilisk/telegram-bot-spring/issues/16
    @Bean
    @ConditionalOnMissingBean(InlineRuleRegistry.class)
    public InlineRuleRegistry inlineRuleRegistry(List<InlineRule> inlineRules) {
        DefaultInlineRuleRegistry defaultInlineRuleRegistry = new DefaultInlineRuleRegistry();
        inlineRules.forEach(defaultInlineRuleRegistry::register);
        return defaultInlineRuleRegistry;
    }

    @Bean
    @ConditionalOnMissingBean(MessageRuleRegistry.class)
    public MessageRuleRegistry messageRuleRegistry(List<MessageRule> messageRules) {
        DefaultMessageRuleRegistry defaultMessageRuleRegistry = new DefaultMessageRuleRegistry();
        messageRules.forEach(defaultMessageRuleRegistry::register);
        return defaultMessageRuleRegistry;
    }

    @Bean
    @ConditionalOnMissingBean(CommandHandlerRegistry.class)
    public CommandHandlerRegistry commandHandlerRegistry(List<CommandUpdateHandler> commandUpdateHandlers) {
        DefaultCommandHandlerRegistry defaultCommandHandlerRegistry = new DefaultCommandHandlerRegistry();
        commandUpdateHandlers.forEach(defaultCommandHandlerRegistry::register);
        return defaultCommandHandlerRegistry;
    }

    @Bean
    @ConditionalOnMissingBean(CallbackHandlerRegistry.class)
    public CallbackHandlerRegistry callbackHandlerRegistry(List<CallbackUpdateHandler> callbackUpdateHandlers) {
        DefaultCallbackHandlerRegistry defaultCallbackHandlerRegistry = new DefaultCallbackHandlerRegistry();
        callbackUpdateHandlers.forEach(defaultCallbackHandlerRegistry::register);
        return defaultCallbackHandlerRegistry;
    }
}
