package io.ksilisk.telegrambot.autoconfigure.config.log;

import io.ksilisk.telegrambot.autoconfigure.logger.Slf4jBotLogger;
import io.ksilisk.telegrambot.core.logger.BotLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class LoggingConfiguration {
    @Bean
    @ConditionalOnMissingBean(BotLogger.class)
    public BotLogger botLogger() {
        return new Slf4jBotLogger();
    }
}
