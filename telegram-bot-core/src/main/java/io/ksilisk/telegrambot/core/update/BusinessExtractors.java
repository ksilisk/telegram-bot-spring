package io.ksilisk.telegrambot.core.update;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.business.BusinessConnection;
import com.pengrad.telegrambot.model.business.BusinessMessageDeleted;

import java.util.Optional;

final class BusinessExtractors {
    private BusinessExtractors() {
    }

    static Optional<BusinessConnection> connection(Update update) {
        return update != null && update.businessConnection() != null
                ? Optional.of(update.businessConnection())
                : Optional.empty();
    }

    static Optional<BusinessMessageDeleted> deletedMessages(Update update) {
        return update != null && update.deletedBusinessMessages() != null
                ? Optional.of(update.deletedBusinessMessages())
                : Optional.empty();
    }

    static Optional<User> user(Update update) {
        return connection(update).map(BusinessConnection::user);
    }

    static Optional<Chat> chat(Update update) {
        return deletedMessages(update).map(BusinessMessageDeleted::chat);
    }

    static Optional<Long> userId(Update update) {
        return user(update).map(User::id);
    }

    static Optional<Long> chatId(Update update) {
        return chat(update).map(Chat::id);
    }
}
