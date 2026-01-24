package io.ksilisk.telegrambot.core.update;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;

import java.util.Optional;

final class MessageLikeExtractors {
    private MessageLikeExtractors() {
    }

    static Optional<Message> message(Update update) {
        if (update == null) {
            return Optional.empty();
        }

        if (update.message() != null) {
            return Optional.of(update.message());
        }
        if (update.editedMessage() != null) {
            return Optional.of(update.editedMessage());
        }
        if (update.channelPost() != null) {
            return Optional.of(update.channelPost());
        }
        if (update.editedChannelPost() != null) {
            return Optional.of(update.editedChannelPost());
        }
        if (update.businessMessage() != null) {
            return Optional.of(update.businessMessage());
        }
        if (update.editedBusinessMessage() != null) {
            return Optional.of(update.editedBusinessMessage());
        }

        return Optional.empty();
    }

    static Optional<User> from(Update update) {
        return message(update).map(Message::from);
    }

    static Optional<Chat> chat(Update update) {
        return message(update).map(Message::chat);
    }

    static Optional<Long> userId(Update update) {
        return from(update).map(User::id);
    }

    static Optional<Long> chatId(Update update) {
        return chat(update).map(Chat::id);
    }
}
