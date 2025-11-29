package io.ksilisk.telegrambot.autoconfigure.config.dispatch;

import io.ksilisk.telegrambot.core.handler.update.callback.CallbackUpdateHandler;
import io.ksilisk.telegrambot.core.handler.update.command.CommandUpdateHandler;
import io.ksilisk.telegrambot.core.registry.handler.callback.CallbackHandlerRegistry;
import io.ksilisk.telegrambot.core.registry.handler.callback.DefaultCallbackHandlerRegistry;
import io.ksilisk.telegrambot.core.registry.handler.command.CommandHandlerRegistry;
import io.ksilisk.telegrambot.core.registry.handler.command.DefaultCommandHandlerRegistry;
import io.ksilisk.telegrambot.core.registry.rule.inline.DefaultInlineUpdateRuleRegistry;
import io.ksilisk.telegrambot.core.registry.rule.inline.InlineUpdateRuleRegistry;
import io.ksilisk.telegrambot.core.registry.rule.message.DefaultMessageUpdateRuleRegistry;
import io.ksilisk.telegrambot.core.registry.rule.message.MessageUpdateRuleRegistry;
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
import io.ksilisk.telegrambot.core.rule.InlineUpdateRule;
import io.ksilisk.telegrambot.core.rule.MessageUpdateRule;
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
                                                       MessageUpdateRuleRegistry messageRuleRegistry,
                                                       CommandDetector commandDetector) {
            return new DefaultMessageUpdateRouter(commandHandlerRegistry, messageRuleRegistry, commandDetector);
        }

        @Bean
        @ConditionalOnMissingBean(MessageUpdateRuleRegistry.class)
        public MessageUpdateRuleRegistry messageRuleRegistry(List<MessageUpdateRule> messageUpdateRules) {
            return new DefaultMessageUpdateRuleRegistry(messageUpdateRules);
        }

        @Bean
        @ConditionalOnMissingBean(CommandHandlerRegistry.class)
        public CommandHandlerRegistry commandHandlerRegistry(List<CommandUpdateHandler> commandUpdateHandlers) {
            return new DefaultCommandHandlerRegistry(commandUpdateHandlers);
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
        public InlineUpdateRouter inlineUpdateRouter(InlineUpdateRuleRegistry inlineRuleRegistry) {
            return new DefaultInlineUpdateRouter(inlineRuleRegistry);
        }

        @Bean
        @ConditionalOnMissingBean(InlineUpdateRuleRegistry.class)
        public InlineUpdateRuleRegistry inlineRuleRegistry(List<InlineUpdateRule> inlineUpdateRules) {
            return new DefaultInlineUpdateRuleRegistry(inlineUpdateRules);
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
            return new DefaultCallbackHandlerRegistry(callbackUpdateHandlers);
        }
    }
}
