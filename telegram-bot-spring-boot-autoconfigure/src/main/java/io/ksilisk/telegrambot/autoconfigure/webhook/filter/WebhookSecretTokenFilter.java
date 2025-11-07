package io.ksilisk.telegrambot.autoconfigure.webhook.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

public class WebhookSecretTokenFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(WebhookSecretTokenFilter.class);

    private static final String TELEGRAM_WEBHOOK_SECRET_TOKEN_HEADER = "X-Telegram-Bot-Api-Secret-Token";

    private final String secretToken;
    private final String webhookEndpoint;

    public WebhookSecretTokenFilter(String secretToken, String webhookEndpoint) {
        this.secretToken = secretToken;
        this.webhookEndpoint = webhookEndpoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!StringUtils.hasText(secretToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestUri = request.getRequestURI();
        if (!Objects.equals(webhookEndpoint, requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        String receivedSecret = request.getHeader(TELEGRAM_WEBHOOK_SECRET_TOKEN_HEADER);

        if (Objects.equals(secretToken, receivedSecret)) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        if (receivedSecret == null) {
            log.warn("Handled request to '{}' endpoint without '{}' header",
                    webhookEndpoint, TELEGRAM_WEBHOOK_SECRET_TOKEN_HEADER);
            response.getWriter().write("No secret token provided by the '" +
                    TELEGRAM_WEBHOOK_SECRET_TOKEN_HEADER + "' header");
        } else {
            log.warn("Handled request to '{}' endpoint with the invalid secret token provided by '{}' header",
                    webhookEndpoint, TELEGRAM_WEBHOOK_SECRET_TOKEN_HEADER);
            response.getWriter().write("Invalid secret token provided by the '" +
                    TELEGRAM_WEBHOOK_SECRET_TOKEN_HEADER + "' header");
        }
    }
}
