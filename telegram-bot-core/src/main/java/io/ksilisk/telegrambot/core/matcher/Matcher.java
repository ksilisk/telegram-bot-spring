package io.ksilisk.telegrambot.core.matcher;

@FunctionalInterface
public interface Matcher<U> {
    boolean match(U update);
}
