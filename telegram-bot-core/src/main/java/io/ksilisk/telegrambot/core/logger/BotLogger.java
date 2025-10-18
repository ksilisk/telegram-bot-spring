package io.ksilisk.telegrambot.core.logger;

public interface BotLogger {
    void info(String msg, Object... args);
    void warn(String msg, Object... args);
    void error(String msg, Throwable ex, Object... args);

    BotLogger NO_OP = new BotLogger() {
        public void info(String m, Object... a) {}
        public void warn(String m, Object... a) {}
        public void error(String m, Throwable e, Object... a) {}
    };
}
