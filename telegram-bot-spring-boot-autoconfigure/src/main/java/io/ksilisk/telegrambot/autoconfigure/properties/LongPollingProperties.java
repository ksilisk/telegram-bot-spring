package io.ksilisk.telegrambot.autoconfigure.properties;

import java.time.Duration;

public class LongPollingProperties {
    private static final Duration DEFAULT_DELAY_DURATION = Duration.ofMillis(200);

    private Duration delay = DEFAULT_DELAY_DURATION;

    public Duration getDelay() {
        return delay;
    }

    public void setDelay(Duration delay) {
        this.delay = delay;
    }
}
