package io.ksilisk.telegrambot.longpolling.failover.impl;

import io.ksilisk.telegrambot.longpolling.failover.MasterFailureClassifier;

import java.io.IOException;

public class DefaultMasterFailureClassifier implements MasterFailureClassifier {
    @Override
    public boolean isFailure(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof IOException) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
}
