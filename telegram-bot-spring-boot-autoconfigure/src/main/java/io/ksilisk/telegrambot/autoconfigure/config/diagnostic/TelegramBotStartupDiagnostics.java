package io.ksilisk.telegrambot.autoconfigure.config.diagnostic;

import io.ksilisk.telegrambot.autoconfigure.properties.TelegramBotProperties;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.interceptor.UpdateInterceptor;
import io.ksilisk.telegrambot.core.rule.UpdateRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;


final class TelegramBotStartupDiagnostics {
    private static final Logger log = LoggerFactory.getLogger(TelegramBotStartupDiagnostics.class);

    static void log(ConfigurableApplicationContext ctx, TelegramBotProperties props) {
        log.info("Configured Telegram bot with mode={}, test-server={}, bot-username={}, client={}",
                props.getMode(), props.getUseTestServer(), props.getBotUsername(), props.getClient().getImplementation());

        log.info("Registered {} handlers, {} rules, {} interceptors",
                ctx.getBeansOfType(UpdateHandler.class).size(),
                ctx.getBeansOfType(UpdateRule.class).size(),
                ctx.getBeansOfType(UpdateInterceptor.class).size());
    }
}
