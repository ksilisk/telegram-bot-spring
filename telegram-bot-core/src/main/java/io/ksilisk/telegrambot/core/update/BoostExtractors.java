package io.ksilisk.telegrambot.core.update;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.chatboost.ChatBoostRemoved;
import com.pengrad.telegrambot.model.chatboost.ChatBoostUpdated;

import java.util.Optional;

final class BoostExtractors {
    private BoostExtractors() {
    }

    static Optional<ChatBoostUpdated> boost(Update update) {
        return update != null && update.chatBoost() != null
                ? Optional.of(update.chatBoost())
                : Optional.empty();
    }

    static Optional<ChatBoostRemoved> removed(Update update) {
        return update != null && update.removedChatBoost() != null
                ? Optional.of(update.removedChatBoost())
                : Optional.empty();
    }

    static Optional<Chat> chat(Update update) {
        return boost(update).map(ChatBoostUpdated::chat)
                .or(() -> removed(update).map(ChatBoostRemoved::chat));
    }

    static Optional<Long> chatId(Update update) {
        return chat(update).map(Chat::id);
    }
}
