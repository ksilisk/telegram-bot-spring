package io.ksilisk.telegrambot.kafka;

import io.ksilisk.telegrambot.kafka.handler.KafkaUpdateExceptionHandler;
import io.ksilisk.telegrambot.kafka.nomatch.KafkaUpdateNoMatchStrategy;
import io.ksilisk.telegrambot.kafka.properties.KafkaProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class TelegramBotKafkaAutoConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "telegram.bot.kafka")
    public KafkaProperties kafkaProperties() {
        return new KafkaProperties();
    }

    @Bean
    @ConditionalOnMissingBean(KafkaUpdateExceptionHandler.class)
    @ConditionalOnProperty(name = "telegram.bot.kafka.exception-topic")
    public KafkaUpdateExceptionHandler kafkaUpdateExceptionHandler(KafkaProperties kafkaProperties) {
        return new KafkaUpdateExceptionHandler(); // todo
    }

    @Bean
    @ConditionalOnMissingBean(KafkaUpdateNoMatchStrategy.class)
    @ConditionalOnProperty(name = "telegram.bot.kafka.no-match-topic")
    public KafkaUpdateNoMatchStrategy kafkaUpdateNoMatchStrategy(KafkaProperties kafkaProperties) {
        return new KafkaUpdateNoMatchStrategy();
    }

}
