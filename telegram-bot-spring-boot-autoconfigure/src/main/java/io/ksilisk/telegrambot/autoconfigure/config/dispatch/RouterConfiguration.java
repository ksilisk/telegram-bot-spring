package io.ksilisk.telegrambot.autoconfigure.config.dispatch;

import io.ksilisk.telegrambot.core.registry.handler.callback.CallbackHandlerRegistry;
import io.ksilisk.telegrambot.core.registry.handler.command.CommandHandlerRegistry;
import io.ksilisk.telegrambot.core.registry.rule.inline.InlineRuleRegistry;
import io.ksilisk.telegrambot.core.registry.rule.message.MessageRuleRegistry;
import io.ksilisk.telegrambot.core.router.CallbackUpdateRouter;
import io.ksilisk.telegrambot.core.router.InlineUpdateRouter;
import io.ksilisk.telegrambot.core.router.MessageUpdateRouter;
import io.ksilisk.telegrambot.core.router.detector.CommandDetector;
import io.ksilisk.telegrambot.core.router.detector.DefaultCommandDetector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RouterConfiguration {

    @Bean
    @ConditionalOnMissingBean(CommandDetector.class)
    public CommandDetector commandDetector() {
        return new DefaultCommandDetector();
    }

    @Bean
    public MessageUpdateRouter messageUpdateRouter(CommandHandlerRegistry commandHandlerRegistry,
                                                   MessageRuleRegistry messageRuleRegistry,
                                                   CommandDetector commandDetector) {
        return new MessageUpdateRouter(commandHandlerRegistry, messageRuleRegistry, commandDetector);
    }

    @Bean
    public CallbackUpdateRouter callbackUpdateRouter(CallbackHandlerRegistry callbackHandlerRegistry) {
        return new CallbackUpdateRouter(callbackHandlerRegistry);
    }

    @Bean
    public InlineUpdateRouter inlineUpdateRouter(InlineRuleRegistry inlineRuleRegistry) {
        return new InlineUpdateRouter(inlineRuleRegistry);
    }
}
