package io.ksilisk.telegrambot.core.update;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.PollAnswer;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;

import java.util.Optional;

final class PollExtractors {
    private PollExtractors() {
    }

    static Optional<PollAnswer> pollAnswer(Update update) {
        return update != null && update.pollAnswer() != null
                ? Optional.of(update.pollAnswer())
                : Optional.empty();
    }

    static Optional<User> user(Update update) {
        return pollAnswer(update).map(PollAnswer::user);
    }

    static Optional<Chat> chat(Update update) {
        return pollAnswer(update).map(PollAnswer::voterChat);
    }

    static Optional<Long> userId(Update update) {
        return user(update).map(User::id);
    }

    static Optional<Long> chatId(Update update) {
        return chat(update).map(Chat::id);
    }
}
