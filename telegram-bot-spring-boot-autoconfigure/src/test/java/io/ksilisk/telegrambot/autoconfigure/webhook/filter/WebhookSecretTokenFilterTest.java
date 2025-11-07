package io.ksilisk.telegrambot.autoconfigure.webhook.filter;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class WebhookSecretTokenFilterTest {
    private static final String SECRET_HEADER = "X-Telegram-Bot-Api-Secret-Token";
    private static final String ENDPOINT = "/telegram/webhook";
    private static final String SECRET = "expected-secret";

    private WebhookSecretTokenFilter filter;

    @BeforeEach
    void setUp() {
        filter = new WebhookSecretTokenFilter(SECRET, ENDPOINT);
    }

    @Test
    void shouldSkipValidationIfSecretNotConfigured() throws Exception {
        var filter = new WebhookSecretTokenFilter("", ENDPOINT);

        MockHttpServletRequest request = new MockHttpServletRequest("POST", ENDPOINT);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    }

    @Test
    void shouldSkipIfRequestUriDoesNotMatchEndpoint() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/another");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    }

    @Test
    void shouldPassIfSecretTokenMatches() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", ENDPOINT);
        request.addHeader(SECRET_HEADER, SECRET);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    }

    @Test
    void shouldRejectIfHeaderMissing() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", ENDPOINT);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_FORBIDDEN);
        assertThat(response.getContentAsString())
                .contains("No secret token provided by");
    }

    @Test
    void shouldRejectIfHeaderInvalid() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", ENDPOINT);
        request.addHeader(SECRET_HEADER, "wrong-secret");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_FORBIDDEN);
        assertThat(response.getContentAsString())
                .contains("Invalid secret token provided by");
    }
}