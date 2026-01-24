package io.ksilisk.telegrambot.core.update;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.MessageReactionCountUpdated;
import com.pengrad.telegrambot.model.MessageReactionUpdated;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;

import java.util.Optional;

final class ReactionExtractors {
    private ReactionExtractors() {
    }

    static Optional<MessageReactionUpdated> reaction(Update update) {
        return update != null && update.messageReaction() != null
                ? Optional.of(update.messageReaction())
                : Optional.empty();
    }

    static Optional<MessageReactionCountUpdated> reactionCount(Update update) {
        return update != null && update.messageReactionCount() != null
                ? Optional.of(update.messageReactionCount())
                : Optional.empty();
    }

    static Optional<User> user(Update update) {
        return reaction(update).map(MessageReactionUpdated::user);
    }

    static Optional<Chat> chat(Update update) {
        return reaction(update).map(MessageReactionUpdated::chat)
                .or(() -> reactionCount(update).map(MessageReactionCountUpdated::chat));
    }

    static Optional<Long> userId(Update update) {
        return user(update).map(User::id);
    }

    static Optional<Long> chatId(Update update) {
        return chat(update).map(Chat::id);
    }
}
