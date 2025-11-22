package io.ksilisk.telegrambot.autoconfigure.config.dispatch;

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
import io.ksilisk.telegrambot.core.router.CallbackUpdateRouter;
import io.ksilisk.telegrambot.core.router.CompositeUpdateRouter;
import io.ksilisk.telegrambot.core.router.InlineUpdateRouter;
import io.ksilisk.telegrambot.core.router.MessageUpdateRouter;
import io.ksilisk.telegrambot.core.router.UpdateRouter;
import io.ksilisk.telegrambot.core.router.detector.CommandDetector;
import io.ksilisk.telegrambot.core.router.detector.DefaultCommandDetector;
import io.ksilisk.telegrambot.core.router.impl.DefaultCallbackUpdateRouter;
import io.ksilisk.telegrambot.core.router.impl.DefaultInlineUpdateRouter;
import io.ksilisk.telegrambot.core.router.impl.DefaultMessageUpdateRouter;
import io.ksilisk.telegrambot.core.rule.InlineRule;
import io.ksilisk.telegrambot.core.rule.MessageRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class RoutingConfiguration {

    @Bean
    @ConditionalOnMissingBean(CompositeUpdateRouter.class)
    public CompositeUpdateRouter compositeUpdateRouter(List<UpdateRouter> updateRouterList) {
        return new CompositeUpdateRouter(updateRouterList);
    }

    @Configuration
    @ConditionalOnProperty(
            prefix = "telegram.bot.routing.message",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public static class MessageRoutingConfiguration {
        @Bean
        @ConditionalOnMissingBean(CommandDetector.class)
        public CommandDetector commandDetector() {
            return new DefaultCommandDetector();
        }

        @Bean
        @ConditionalOnMissingBean(MessageUpdateRouter.class)
        public MessageUpdateRouter messageUpdateRouter(CommandHandlerRegistry commandHandlerRegistry,
                                                       MessageRuleRegistry messageRuleRegistry,
                                                       CommandDetector commandDetector) {
            return new DefaultMessageUpdateRouter(commandHandlerRegistry, messageRuleRegistry, commandDetector);
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
    }

    @Configuration
    @ConditionalOnProperty(
            prefix = "telegram.bot.routing.inline",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public static class InlineRoutingConfiguration {
        @Bean
        @ConditionalOnMissingBean(InlineUpdateRouter.class)
        public InlineUpdateRouter inlineUpdateRouter(InlineRuleRegistry inlineRuleRegistry) {
            return new DefaultInlineUpdateRouter(inlineRuleRegistry);
        }

        @Bean
        @ConditionalOnMissingBean(InlineRuleRegistry.class)
        public InlineRuleRegistry inlineRuleRegistry(List<InlineRule> inlineRules) {
            DefaultInlineRuleRegistry defaultInlineRuleRegistry = new DefaultInlineRuleRegistry();
            inlineRules.forEach(defaultInlineRuleRegistry::register);
            return defaultInlineRuleRegistry;
        }
    }

    @Configuration
    @ConditionalOnProperty(
            prefix = "telegram.bot.routing.callback",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public static class CallbackRoutingConfiguration {

        @Bean
        @ConditionalOnMissingBean(CallbackUpdateRouter.class)
        public CallbackUpdateRouter callbackUpdateRouter(CallbackHandlerRegistry callbackHandlerRegistry) {
            return new DefaultCallbackUpdateRouter(callbackHandlerRegistry);
        }

        @Bean
        @ConditionalOnMissingBean(CallbackHandlerRegistry.class)
        public CallbackHandlerRegistry callbackHandlerRegistry(List<CallbackUpdateHandler> callbackUpdateHandlers) {
            DefaultCallbackHandlerRegistry defaultCallbackHandlerRegistry = new DefaultCallbackHandlerRegistry();
            callbackUpdateHandlers.forEach(defaultCallbackHandlerRegistry::register);
            return defaultCallbackHandlerRegistry;
        }
    }
}
