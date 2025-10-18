package io.ksilisk.telegrambot.core.handler.update.command;

import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;

import java.util.Set;

public interface CommandUpdateHandler extends UpdateHandler {
    Set<String> commands();
}
