package io.ksilisk.telegrambot.core.ingress;

/**
 * An implementation of UpdateIngress that does nothing.
 * Used when the bot is configured to not receive any updates.
 */
public class NoIngressUpdateIngress implements UpdateIngress {
    public NoIngressUpdateIngress() {
    }
}
