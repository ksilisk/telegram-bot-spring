package io.ksilisk.telegrambot.core.update;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.ChatJoinRequest;
import com.pengrad.telegrambot.model.ChatMemberUpdated;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;

import java.util.Optional;

final class ChatStateExtractors {
    private ChatStateExtractors() {
    }

    static Optional<ChatMemberUpdated> chatMember(Update update) {
        return update != null && update.chatMember() != null
                ? Optional.of(update.chatMember())
                : Optional.empty();
    }

    static Optional<ChatMemberUpdated> myChatMember(Update update) {
        return update != null && update.myChatMember() != null
                ? Optional.of(update.myChatMember())
                : Optional.empty();
    }

    static Optional<ChatJoinRequest> chatJoinRequest(Update update) {
        return update != null && update.chatJoinRequest() != null
                ? Optional.of(update.chatJoinRequest())
                : Optional.empty();
    }

    static Optional<User> from(Update update) {
        return chatMember(update).map(ChatMemberUpdated::from)
                .or(() -> myChatMember(update).map(ChatMemberUpdated::from))
                .or(() -> chatJoinRequest(update).map(ChatJoinRequest::from));
    }

    static Optional<Chat> chat(Update update) {
        return chatMember(update).map(ChatMemberUpdated::chat)
                .or(() -> myChatMember(update).map(ChatMemberUpdated::chat))
                .or(() -> chatJoinRequest(update).map(ChatJoinRequest::chat));
    }

    static Optional<Long> userId(Update update) {
        return from(update).map(User::id);
    }

    static Optional<Long> chatId(Update update) {
        return chat(update).map(Chat::id);
    }
}
