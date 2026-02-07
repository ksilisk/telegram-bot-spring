package io.ksilisk.telegrambot.examples.advanced.longpolling.handler.message;

import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendPhoto;
import io.ksilisk.telegrambot.core.executor.TelegramBotExecutor;
import io.ksilisk.telegrambot.core.file.TelegramBotFileClient;
import io.ksilisk.telegrambot.core.handler.update.message.MessageUpdateHandler;
import io.ksilisk.telegrambot.core.update.Updates;
import org.springframework.stereotype.Component;

@Component
public class PhotoMessageUpdateHandler implements MessageUpdateHandler {
    private final TelegramBotExecutor telegramBotExecutor;
    private final TelegramBotFileClient fileClient;

    public PhotoMessageUpdateHandler(TelegramBotExecutor telegramBotExecutor, TelegramBotFileClient fileClient) {
        this.telegramBotExecutor = telegramBotExecutor;
        this.fileClient = fileClient;
    }

    @Override
    public void handle(Update update) {
        PhotoSize[] photoSizes = update.message().photo();
        PhotoSize maxSizePhoto = photoSizes[photoSizes.length - 1];

        byte[] bytes = fileClient.downloadById(maxSizePhoto.fileId());

        SendPhoto sendPhoto = new SendPhoto(Updates.chatId(update), bytes);
        sendPhoto.caption("Here's your photo!");

        telegramBotExecutor.execute(sendPhoto);
    }
}
