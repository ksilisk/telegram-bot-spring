package io.ksilisk.telegrambot.core.executor.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultSwitchableTelegramBotApiUrlProvider implements SwitchableTelegramBotApiUrlProvider {
    private static final Logger log = LoggerFactory.getLogger(DefaultSwitchableTelegramBotApiUrlProvider.class);

    private static final String DEFAULT_ENDPOINT = "https://api.telegram.org";

    private final List<String> endpoints;
    private final String botToken;
    private final boolean testServer;

    private final AtomicInteger currentIndex = new AtomicInteger();

    public DefaultSwitchableTelegramBotApiUrlProvider(String botToken, boolean testServer, List<String> endpoints) {
        this.botToken = botToken;
        this.testServer = testServer;
        this.endpoints = normalizeEndpoints(endpoints);
    }

    public DefaultSwitchableTelegramBotApiUrlProvider(String botToken, boolean testServer) {
        this(botToken, testServer, List.of(DEFAULT_ENDPOINT));
    }

    @Override
    public void switchToNext() {
        if (endpoints.size() <= 1) {
            log.debug("Telegram Bot API endpoint switch skipped: only one endpoint configured");
            return;
        }

        int previousIndex = currentIndex.getAndUpdate(index -> (index + 1) % endpoints.size());
        int current = currentIndex.get();

        log.warn("Telegram Bot API endpoint switched: {} -> {}", endpoints.get(previousIndex), endpoints.get(current));
    }

    @Override
    public String getApiUrl() {
        return currentEndpoint() + "/bot" + botToken + suffix();
    }

    @Override
    public String getFileUrl() {
        return currentEndpoint() + "/file/bot" + botToken + suffix();
    }

    private String currentEndpoint() {
        return endpoints.get(currentIndex.get());
    }

    private String suffix() {
        return testServer ? "/test/" : "/";
    }

    private static List<String> normalizeEndpoints(List<String> endpoints) {
        if (endpoints == null || endpoints.isEmpty()) {
            return List.of(DEFAULT_ENDPOINT);

        }

        List<String> normalized = new ArrayList<>(endpoints.size());
        for (String endpoint : endpoints) {
            String value = normalizeEndpoint(endpoint);
            if (!normalized.contains(value)) {
                normalized.add(value);
            }
        }

        return List.copyOf(normalized);
    }

    private static String normalizeEndpoint(String endpoint) {
        while (endpoint.endsWith("/")) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }
        return endpoint;
    }
}
