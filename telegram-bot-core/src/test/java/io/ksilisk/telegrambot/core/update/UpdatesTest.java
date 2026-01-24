package io.ksilisk.telegrambot.core.update;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.business.BusinessConnection;
import com.pengrad.telegrambot.model.business.BusinessMessageDeleted;
import com.pengrad.telegrambot.model.chatboost.ChatBoostRemoved;
import com.pengrad.telegrambot.model.chatboost.ChatBoostUpdated;
import com.pengrad.telegrambot.model.message.MaybeInaccessibleMessage;
import com.pengrad.telegrambot.model.paidmedia.PaidMediaPurchased;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UpdatesTest {
    @Test
    void nullUpdate_isSafe() {
        assertThat(Updates.type(null)).isEqualTo(UpdateType.UNKNOWN);
        assertThat(Updates.safeUserId(null)).isEmpty();
        assertThat(Updates.safeChatId(null)).isEmpty();
    }

    @Nested
    class UpdatesTypeTest {

        static Stream<org.junit.jupiter.params.provider.Arguments> typeCases() {
            return Stream.of(
                    arguments(mockUpdateWith(u -> when(u.message()).thenReturn(mock(Message.class))), UpdateType.MESSAGE),
                    arguments(mockUpdateWith(u -> when(u.editedMessage()).thenReturn(mock(Message.class))), UpdateType.EDITED_MESSAGE),
                    arguments(mockUpdateWith(u -> when(u.channelPost()).thenReturn(mock(Message.class))), UpdateType.CHANNEL_POST),
                    arguments(mockUpdateWith(u -> when(u.editedChannelPost()).thenReturn(mock(Message.class))), UpdateType.EDITED_CHANNEL_POST),

                    arguments(mockUpdateWith(u -> when(u.businessConnection()).thenReturn(mock(BusinessConnection.class))), UpdateType.BUSINESS_CONNECTION),
                    arguments(mockUpdateWith(u -> when(u.businessMessage()).thenReturn(mock(Message.class))), UpdateType.BUSINESS_MESSAGE),
                    arguments(mockUpdateWith(u -> when(u.editedBusinessMessage()).thenReturn(mock(Message.class))), UpdateType.EDITED_BUSINESS_MESSAGE),
                    arguments(mockUpdateWith(u -> when(u.deletedBusinessMessages()).thenReturn(mock(BusinessMessageDeleted.class))), UpdateType.DELETED_BUSINESS_MESSAGES),

                    arguments(mockUpdateWith(u -> when(u.messageReaction()).thenReturn(mock(MessageReactionUpdated.class))), UpdateType.MESSAGE_REACTION),
                    arguments(mockUpdateWith(u -> when(u.messageReactionCount()).thenReturn(mock(MessageReactionCountUpdated.class))), UpdateType.MESSAGE_REACTION_COUNT),

                    arguments(mockUpdateWith(u -> when(u.callbackQuery()).thenReturn(mock(CallbackQuery.class))), UpdateType.CALLBACK_QUERY),
                    arguments(mockUpdateWith(u -> when(u.inlineQuery()).thenReturn(mock(InlineQuery.class))), UpdateType.INLINE_QUERY),
                    arguments(mockUpdateWith(u -> when(u.chosenInlineResult()).thenReturn(mock(ChosenInlineResult.class))), UpdateType.CHOSEN_INLINE_RESULT),

                    arguments(mockUpdateWith(u -> when(u.shippingQuery()).thenReturn(mock(ShippingQuery.class))), UpdateType.SHIPPING_QUERY),
                    arguments(mockUpdateWith(u -> when(u.preCheckoutQuery()).thenReturn(mock(PreCheckoutQuery.class))), UpdateType.PRE_CHECKOUT_QUERY),

                    arguments(mockUpdateWith(u -> when(u.purchasedPaidMedia()).thenReturn(mock(PaidMediaPurchased.class))), UpdateType.PURCHASED_PAID_MEDIA),

                    arguments(mockUpdateWith(u -> when(u.poll()).thenReturn(mock(Poll.class))), UpdateType.POLL),
                    arguments(mockUpdateWith(u -> when(u.pollAnswer()).thenReturn(mock(PollAnswer.class))), UpdateType.POLL_ANSWER),

                    arguments(mockUpdateWith(u -> when(u.chatMember()).thenReturn(mock(ChatMemberUpdated.class))), UpdateType.CHAT_MEMBER),
                    arguments(mockUpdateWith(u -> when(u.myChatMember()).thenReturn(mock(ChatMemberUpdated.class))), UpdateType.MY_CHAT_MEMBER),
                    arguments(mockUpdateWith(u -> when(u.chatJoinRequest()).thenReturn(mock(ChatJoinRequest.class))), UpdateType.CHAT_JOIN_REQUEST),

                    arguments(mockUpdateWith(u -> when(u.chatBoost()).thenReturn(mock(ChatBoostUpdated.class))), UpdateType.CHAT_BOOST),
                    arguments(mockUpdateWith(u -> when(u.removedChatBoost()).thenReturn(mock(ChatBoostRemoved.class))), UpdateType.REMOVED_CHAT_BOOST),

                    arguments(mock(Update.class), UpdateType.UNKNOWN)
            );
        }

        @ParameterizedTest
        @MethodSource("typeCases")
        void resolvesType(Update update, UpdateType expected) {
            assertThat(Updates.type(update)).isEqualTo(expected);
        }

        @Test
        void nullUpdate_isUnknown() {
            assertThat(Updates.type(null)).isEqualTo(UpdateType.UNKNOWN);
        }

        private static Update mockUpdateWith(java.util.function.Consumer<Update> stubbing) {
            Update u = mock(Update.class);
            stubbing.accept(u);
            return u;
        }
    }

    @Nested
    class UpdatesIdsMessageLikeTest {

        @Test
        void message_hasUserAndChat() {
            Update u = mock(Update.class);
            when(u.message()).thenReturn(mock(Message.class)); // важно для type()

            Message m = u.message();
            User from = mock(User.class);
            Chat chat = mock(Chat.class);

            when(m.from()).thenReturn(from);
            when(from.id()).thenReturn(100L);

            when(m.chat()).thenReturn(chat);
            when(chat.id()).thenReturn(200L);

            assertThat(Updates.safeUserId(u)).contains(100L);
            assertThat(Updates.safeChatId(u)).contains(200L);
        }

        @Test
        void channelPost_hasChatButNoUser() {
            Update u = mock(Update.class);
            when(u.channelPost()).thenReturn(mock(Message.class));

            Message m = u.channelPost();
            when(m.from()).thenReturn(null); // типичный кейс канала

            Chat chat = mock(Chat.class);
            when(m.chat()).thenReturn(chat);
            when(chat.id()).thenReturn(300L);

            assertThat(Updates.safeUserId(u)).isEmpty();
            assertThat(Updates.safeChatId(u)).contains(300L);
        }
    }

    @Nested
    class UpdatesIdsCallbackTest {

        @Test
        void callbackMessageBased_hasUserAndChat() {
            Update u = mock(Update.class);
            CallbackQuery cq = mock(CallbackQuery.class);
            when(u.callbackQuery()).thenReturn(cq);

            User from = mock(User.class);
            when(cq.from()).thenReturn(from);
            when(from.id()).thenReturn(10L);

            MaybeInaccessibleMessage msg = mock(MaybeInaccessibleMessage.class);
            when(cq.maybeInaccessibleMessage()).thenReturn(msg);

            Chat chat = mock(Chat.class);
            when(msg.chat()).thenReturn(chat);
            when(chat.id()).thenReturn(20L);

            assertThat(Updates.safeUserId(u)).contains(10L);
            assertThat(Updates.safeChatId(u)).contains(20L);
        }

        @Test
        void callbackInline_hasUserButNoChat() {
            Update u = mock(Update.class);
            CallbackQuery cq = mock(CallbackQuery.class);
            when(u.callbackQuery()).thenReturn(cq);

            User from = mock(User.class);
            when(cq.from()).thenReturn(from);
            when(from.id()).thenReturn(11L);

            when(cq.maybeInaccessibleMessage()).thenReturn(null); // inline callback => no message => no chat

            assertThat(Updates.safeUserId(u)).contains(11L);
            assertThat(Updates.safeChatId(u)).isEmpty();
        }
    }

    @Nested
    class UpdatesIdsInlineTest {

        @Test
        void inlineQuery_hasUserNoChat() {
            Update u = mock(Update.class);
            InlineQuery iq = mock(InlineQuery.class);
            when(u.inlineQuery()).thenReturn(iq);

            User from = mock(User.class);
            when(iq.from()).thenReturn(from);
            when(from.id()).thenReturn(42L);

            assertThat(Updates.safeUserId(u)).contains(42L);
            assertThat(Updates.safeChatId(u)).isEmpty();
        }

        @Test
        void chosenInlineResult_hasUserNoChat() {
            Update u = mock(Update.class);
            ChosenInlineResult cir = mock(ChosenInlineResult.class);
            when(u.chosenInlineResult()).thenReturn(cir);

            User from = mock(User.class);
            when(cir.from()).thenReturn(from);
            when(from.id()).thenReturn(43L);

            assertThat(Updates.safeUserId(u)).contains(43L);
            assertThat(Updates.safeChatId(u)).isEmpty();
        }
    }

    @Nested
    class UpdatesIdsPollAnswerTest {

        @Test
        void pollAnswer_withUser_hasUserNoChat() {
            Update u = mock(Update.class);
            PollAnswer pa = mock(PollAnswer.class);
            when(u.pollAnswer()).thenReturn(pa);

            User user = mock(User.class);
            when(pa.user()).thenReturn(user);
            when(user.id()).thenReturn(50L);

            when(pa.voterChat()).thenReturn(null);

            assertThat(Updates.safeUserId(u)).contains(50L);
            assertThat(Updates.safeChatId(u)).isEmpty();
        }

        @Test
        void pollAnswer_withVoterChat_hasChatNoUser() {
            Update u = mock(Update.class);
            PollAnswer pa = mock(PollAnswer.class);
            when(u.pollAnswer()).thenReturn(pa);

            when(pa.user()).thenReturn(null);

            Chat chat = mock(Chat.class);
            when(pa.voterChat()).thenReturn(chat);
            when(chat.id()).thenReturn(60L);

            assertThat(Updates.safeUserId(u)).isEmpty();
            assertThat(Updates.safeChatId(u)).contains(60L);
        }
    }

}
