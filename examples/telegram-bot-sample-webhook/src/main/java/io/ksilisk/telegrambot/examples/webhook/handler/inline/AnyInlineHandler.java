package io.ksilisk.telegrambot.examples.webhook.handler.inline;

import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.handler.update.inline.InlineUpdateHandler;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AnyInlineHandler implements InlineUpdateHandler {
    private final TelegramBotExecutor telegramBotExecutor;

    public AnyInlineHandler(TelegramBotExecutor telegramBotExecutor) {
        this.telegramBotExecutor = telegramBotExecutor;
    }

    @Override
    public void handle(Update update) {
        InlineQuery inlineQuery = update.inlineQuery();

        InlineQueryResultArticle inlineQueryResultArticle = new InlineQueryResultArticle(
                UUID.randomUUID().toString(), "Your simple inline query result", inlineQuery.query());

        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery(inlineQuery.id(), inlineQueryResultArticle);

        telegramBotExecutor.execute(answerInlineQuery);
    }
}
