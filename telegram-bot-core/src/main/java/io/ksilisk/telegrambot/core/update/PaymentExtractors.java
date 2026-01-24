package io.ksilisk.telegrambot.core.update;

import com.pengrad.telegrambot.model.PreCheckoutQuery;
import com.pengrad.telegrambot.model.ShippingQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;

import java.util.Optional;

final class PaymentExtractors {
    private PaymentExtractors() {
    }

    static Optional<ShippingQuery> shipping(Update update) {
        return update != null && update.shippingQuery() != null
                ? Optional.of(update.shippingQuery())
                : Optional.empty();
    }

    static Optional<PreCheckoutQuery> preCheckout(Update update) {
        return update != null && update.preCheckoutQuery() != null
                ? Optional.of(update.preCheckoutQuery())
                : Optional.empty();
    }

    static Optional<User> from(Update update) {
        return shipping(update).map(ShippingQuery::from)
                .or(() -> preCheckout(update).map(PreCheckoutQuery::from));
    }

    static Optional<Long> userId(Update update) {
        return from(update).map(User::id);
    }
}
