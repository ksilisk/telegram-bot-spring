package io.ksilisk.telegrambot.core.update;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.message.MaybeInaccessibleMessage;

import java.util.Optional;

final class CallbackExtractors {
    private CallbackExtractors() {
    }

    static Optional<CallbackQuery> callback(Update update) {
        return update != null && update.callbackQuery() != null
                ? Optional.of(update.callbackQuery())
                : Optional.empty();
    }

    static Optional<User> from(Update update) {
        return callback(update).map(CallbackQuery::from);
    }

    static Optional<MaybeInaccessibleMessage> message(Update update) {
        return callback(update).map(CallbackQuery::maybeInaccessibleMessage);
    }

    static Optional<Chat> chat(Update update) {
        return message(update).map(MaybeInaccessibleMessage::chat);
    }

    static Optional<Long> userId(Update update) {
        return from(update).map(User::id);
    }

    static Optional<Long> chatId(Update update) {
        return chat(update).map(Chat::id);
    }
}
