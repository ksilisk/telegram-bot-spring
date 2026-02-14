package io.ksilisk.telegrambot.autoconfigure.customizer;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.annotation.OrderUtils;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class OkHttpClientCustomizerTest {
    @Test
    void shouldApplyCustomizersInOrderAndAffectBuiltClient() {
        // given
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClientCustomizer c1 = new TimeoutAndFirstInterceptorCustomizer();
        OkHttpClientCustomizer c2 = new SecondInterceptorCustomizer();
        List<OkHttpClientCustomizer> customizers = Stream.of(c2, c1)
                .sorted(Comparator.comparingInt(OkHttpClientCustomizerTest::orderOf))
                .toList();

        // when
        customizers.forEach(c -> c.customize(builder));
        OkHttpClient client = builder.build();

        // then
        assertThat(client.callTimeoutMillis())
                .isEqualTo((int) TimeUnit.SECONDS.toMillis(42));
        assertThat(client.interceptors())
                .extracting(i -> i.getClass().getSimpleName())
                .containsSequence("FirstInterceptor", "SecondInterceptor");
    }

    private static int orderOf(Object bean) {
        Integer order = OrderUtils.getOrder(bean.getClass());
        if (order != null) return order;
        if (bean instanceof Ordered o) return o.getOrder();
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Order(0)
    static final class TimeoutAndFirstInterceptorCustomizer implements OkHttpClientCustomizer {
        @Override
        public void customize(OkHttpClient.Builder builder) {
            builder.callTimeout(42, TimeUnit.SECONDS);
            builder.addInterceptor(new FirstInterceptor());
        }
    }

    @Order(1)
    static final class SecondInterceptorCustomizer implements OkHttpClientCustomizer {
        @Override
        public void customize(OkHttpClient.Builder builder) {
            builder.addInterceptor(new SecondInterceptor());
        }
    }

    static final class FirstInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request req = chain.request();
            return chain.proceed(req);
        }
    }

    static final class SecondInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request req = chain.request();
            return chain.proceed(req);
        }
    }

    @FunctionalInterface
    interface OkHttpClientCustomizer extends Ordered {
        void customize(OkHttpClient.Builder builder);

        @Override
        default int getOrder() {
            return LOWEST_PRECEDENCE;
        }
    }
}
