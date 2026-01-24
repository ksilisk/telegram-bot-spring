package io.ksilisk.telegrambot.core.update;

public enum UpdateType {
    // Message
    MESSAGE,
    EDITED_MESSAGE,
    CHANNEL_POST,
    EDITED_CHANNEL_POST,

    // Business
    BUSINESS_CONNECTION,
    BUSINESS_MESSAGE,
    EDITED_BUSINESS_MESSAGE,
    DELETED_BUSINESS_MESSAGES,

    // Reactions
    MESSAGE_REACTION,
    MESSAGE_REACTION_COUNT,

    // Interactive
    CALLBACK_QUERY,
    INLINE_QUERY,
    CHOSEN_INLINE_RESULT,

    // Payments
    SHIPPING_QUERY,
    PRE_CHECKOUT_QUERY,

    // Paid media
    PURCHASED_PAID_MEDIA,

    // Polls
    POLL,
    POLL_ANSWER,

    // Chat state
    CHAT_MEMBER,
    MY_CHAT_MEMBER,
    CHAT_JOIN_REQUEST,

    // Boosts
    CHAT_BOOST,
    REMOVED_CHAT_BOOST,

    // Fallback
    UNKNOWN
}
