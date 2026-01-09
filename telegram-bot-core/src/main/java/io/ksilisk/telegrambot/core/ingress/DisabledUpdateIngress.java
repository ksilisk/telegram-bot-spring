package io.ksilisk.telegrambot.core.ingress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of UpdateIngress that does nothing.
 * Used when the bot is configured to not receive any updates.
 */
public class DisabledUpdateIngress implements UpdateIngress {

    private static final Logger log = LoggerFactory.getLogger(DisabledUpdateIngress.class);

    public DisabledUpdateIngress() {
        log.info("Ingress is configured to DISABLED. The bot will not receive updates.");
    }
}
