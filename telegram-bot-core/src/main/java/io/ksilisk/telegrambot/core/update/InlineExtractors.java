package io.ksilisk.telegrambot.core.update;

import com.pengrad.telegrambot.model.ChosenInlineResult;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;

import java.util.Optional;

final class InlineExtractors {
    private InlineExtractors() {
    }

    static Optional<InlineQuery> inlineQuery(Update update) {
        return update != null && update.inlineQuery() != null
                ? Optional.of(update.inlineQuery())
                : Optional.empty();
    }

    static Optional<ChosenInlineResult> chosenInlineResult(Update update) {
        return update != null && update.chosenInlineResult() != null
                ? Optional.of(update.chosenInlineResult())
                : Optional.empty();
    }

    static Optional<User> from(Update update) {
        return inlineQuery(update).map(InlineQuery::from)
                .or(() -> chosenInlineResult(update).map(ChosenInlineResult::from));
    }

    static Optional<Long> userId(Update update) {
        return from(update).map(User::id);
    }
}
