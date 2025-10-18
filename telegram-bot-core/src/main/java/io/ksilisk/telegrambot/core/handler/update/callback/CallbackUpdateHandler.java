package io.ksilisk.telegrambot.core.handler.update.callback;

import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;

import java.util.Set;

public interface CallbackUpdateHandler extends UpdateHandler {
    Set<String> callbacks();
}
