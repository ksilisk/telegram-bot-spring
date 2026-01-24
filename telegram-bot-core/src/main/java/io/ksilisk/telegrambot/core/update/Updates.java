package io.ksilisk.telegrambot.core.update;

import com.pengrad.telegrambot.model.Update;

import java.util.Optional;

import static io.ksilisk.telegrambot.core.update.UpdateType.BUSINESS_CONNECTION;
import static io.ksilisk.telegrambot.core.update.UpdateType.BUSINESS_MESSAGE;
import static io.ksilisk.telegrambot.core.update.UpdateType.CALLBACK_QUERY;
import static io.ksilisk.telegrambot.core.update.UpdateType.CHANNEL_POST;
import static io.ksilisk.telegrambot.core.update.UpdateType.CHAT_BOOST;
import static io.ksilisk.telegrambot.core.update.UpdateType.CHAT_JOIN_REQUEST;
import static io.ksilisk.telegrambot.core.update.UpdateType.CHAT_MEMBER;
import static io.ksilisk.telegrambot.core.update.UpdateType.CHOSEN_INLINE_RESULT;
import static io.ksilisk.telegrambot.core.update.UpdateType.DELETED_BUSINESS_MESSAGES;
import static io.ksilisk.telegrambot.core.update.UpdateType.EDITED_BUSINESS_MESSAGE;
import static io.ksilisk.telegrambot.core.update.UpdateType.EDITED_CHANNEL_POST;
import static io.ksilisk.telegrambot.core.update.UpdateType.EDITED_MESSAGE;
import static io.ksilisk.telegrambot.core.update.UpdateType.INLINE_QUERY;
import static io.ksilisk.telegrambot.core.update.UpdateType.MESSAGE;
import static io.ksilisk.telegrambot.core.update.UpdateType.MESSAGE_REACTION;
import static io.ksilisk.telegrambot.core.update.UpdateType.MESSAGE_REACTION_COUNT;
import static io.ksilisk.telegrambot.core.update.UpdateType.MY_CHAT_MEMBER;
import static io.ksilisk.telegrambot.core.update.UpdateType.POLL;
import static io.ksilisk.telegrambot.core.update.UpdateType.POLL_ANSWER;
import static io.ksilisk.telegrambot.core.update.UpdateType.PRE_CHECKOUT_QUERY;
import static io.ksilisk.telegrambot.core.update.UpdateType.PURCHASED_PAID_MEDIA;
import static io.ksilisk.telegrambot.core.update.UpdateType.REMOVED_CHAT_BOOST;
import static io.ksilisk.telegrambot.core.update.UpdateType.SHIPPING_QUERY;
import static io.ksilisk.telegrambot.core.update.UpdateType.UNKNOWN;


/**
 * Utility class providing null-safe accessors for Telegram {@link Update}.
 *
 * <p>This class centralizes extraction of commonly used identifiers
 * (such as userId and chatId) and update type detection, hiding
 * Telegram API nullable fields behind a stable, defensive API.</p>
 *
 * <p>The methods are best-effort: if an update does not carry the requested
 * information by design, {@link Optional#empty()} is returned.</p>
 *
 * <p>This class contains no state and has no side effects.</p>
 */
public final class Updates {

    /**
     * Attempts to extract a Telegram user identifier from the given update.
     *
     * <p>The returned value represents the user who initiated the update,
     * when such user exists according to the Telegram Bot API.</p>
     *
     * <p>For update types that are not associated with a user
     * (e.g. polls, reaction count updates, inline-only events),
     * this method returns {@link Optional#empty()}.</p>
     *
     * @param update Telegram update
     * @return optional user id
     */
    public static Optional<Long> safeUserId(Update update) {
        if (update == null) {
            return Optional.empty();
        }

        return switch (type(update)) {

            case MESSAGE,
                 EDITED_MESSAGE,
                 CHANNEL_POST,
                 EDITED_CHANNEL_POST,
                 BUSINESS_MESSAGE,
                 EDITED_BUSINESS_MESSAGE -> MessageLikeExtractors.userId(update);

            case BUSINESS_CONNECTION -> BusinessExtractors.userId(update);

            case CALLBACK_QUERY -> CallbackExtractors.userId(update);

            case INLINE_QUERY,
                 CHOSEN_INLINE_RESULT -> InlineExtractors.userId(update);

            case SHIPPING_QUERY,
                 PRE_CHECKOUT_QUERY -> PaymentExtractors.userId(update);

            case POLL_ANSWER -> PollExtractors.userId(update);

            case CHAT_MEMBER,
                 MY_CHAT_MEMBER,
                 CHAT_JOIN_REQUEST -> ChatStateExtractors.userId(update);

            case MESSAGE_REACTION -> ReactionExtractors.userId(update);

            default -> Optional.empty();
        };
    }

    /**
     * Convenience method returning user id or {@code null} if the update
     * is not associated with a user.
     *
     * <p>Prefer {@link #safeUserId(Update)} for core logic.</p>
     */
    public static Long userId(Update update) {
        return safeUserId(update).orElse(null);
    }

    /**
     * Attempts to extract a Telegram chat identifier from the given update.
     *
     * <p>The returned value represents the chat where the update occurred,
     * when such chat exists according to the Telegram Bot API.</p>
     *
     * <p>For update types that are not associated with a chat
     * (e.g. inline queries, chosen inline results, payment queries),
     * this method returns {@link Optional#empty()}.</p>
     *
     * @param update Telegram update
     * @return optional chat id
     */
    public static Optional<Long> safeChatId(Update update) {
        if (update == null) {
            return Optional.empty();
        }

        return switch (type(update)) {
            case MESSAGE, EDITED_MESSAGE, CHANNEL_POST,
                 EDITED_CHANNEL_POST, BUSINESS_MESSAGE,
                 EDITED_BUSINESS_MESSAGE -> MessageLikeExtractors.chatId(update);

            case DELETED_BUSINESS_MESSAGES,
                 BUSINESS_CONNECTION -> BusinessExtractors.chatId(update);

            case CALLBACK_QUERY -> CallbackExtractors.chatId(update);

            case POLL_ANSWER -> PollExtractors.chatId(update);

            case CHAT_MEMBER,
                 MY_CHAT_MEMBER,
                 CHAT_JOIN_REQUEST -> ChatStateExtractors.chatId(update);

            case MESSAGE_REACTION,
                 MESSAGE_REACTION_COUNT -> ReactionExtractors.chatId(update);

            case CHAT_BOOST,
                 REMOVED_CHAT_BOOST -> BoostExtractors.chatId(update);

            default -> Optional.empty();
        };
    }

    /**
     * Convenience method returning chat id or {@code null} if the update
     * is not associated with a chat.
     *
     * <p>Prefer {@link #safeChatId(Update)} for core logic.</p>
     */
    public static Long chatId(Update update) {
        return safeChatId(update).orElse(null);
    }

    /**
     * Determines the {@link UpdateType} of the given Telegram update.
     *
     * <p>The type is resolved by inspecting which payload field of the update
     * is present. According to the Telegram Bot API contract, at most one
     * such field is non-null.</p>
     *
     * <p>If the update does not match any known type, {@link UpdateType#UNKNOWN}
     * is returned.</p>
     *
     * @param update Telegram update
     * @return resolved update type
     */
    public static UpdateType type(Update update) {
        if (update == null) {
            return UNKNOWN;
        }

        // Message
        if (update.message() != null) {
            return MESSAGE;
        }
        if (update.editedMessage() != null) {
            return EDITED_MESSAGE;
        }
        if (update.channelPost() != null) {
            return CHANNEL_POST;
        }
        if (update.editedChannelPost() != null) {
            return EDITED_CHANNEL_POST;
        }

        // Business
        if (update.businessConnection() != null) {
            return BUSINESS_CONNECTION;
        }
        if (update.businessMessage() != null) {
            return BUSINESS_MESSAGE;
        }
        if (update.editedBusinessMessage() != null) {
            return EDITED_BUSINESS_MESSAGE;
        }
        if (update.deletedBusinessMessages() != null) {
            return DELETED_BUSINESS_MESSAGES;
        }

        // Reactions
        if (update.messageReaction() != null) {
            return MESSAGE_REACTION;
        }
        if (update.messageReactionCount() != null) {
            return MESSAGE_REACTION_COUNT;
        }

        // Interactive
        if (update.callbackQuery() != null) {
            return CALLBACK_QUERY;
        }
        if (update.inlineQuery() != null) {
            return INLINE_QUERY;
        }
        if (update.chosenInlineResult() != null) {
            return CHOSEN_INLINE_RESULT;
        }

        // Payments
        if (update.shippingQuery() != null) {
            return SHIPPING_QUERY;
        }
        if (update.preCheckoutQuery() != null) {
            return PRE_CHECKOUT_QUERY;
        }

        // Paid media
        if (update.purchasedPaidMedia() != null) {
            return PURCHASED_PAID_MEDIA;
        }

        // Polls
        if (update.poll() != null) {
            return POLL;
        }
        if (update.pollAnswer() != null) {
            return POLL_ANSWER;
        }

        // Chat state
        if (update.chatMember() != null) {
            return CHAT_MEMBER;
        }
        if (update.myChatMember() != null) {
            return MY_CHAT_MEMBER;
        }
        if (update.chatJoinRequest() != null) {
            return CHAT_JOIN_REQUEST;
        }

        // Boosts
        if (update.chatBoost() != null) {
            return CHAT_BOOST;
        }
        if (update.removedChatBoost() != null) {
            return REMOVED_CHAT_BOOST;
        }

        return UNKNOWN;
    }

    private Updates() {
    }
}
