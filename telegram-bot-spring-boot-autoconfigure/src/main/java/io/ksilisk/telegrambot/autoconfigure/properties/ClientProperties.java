package io.ksilisk.telegrambot.autoconfigure.properties;

public class ClientProperties {
    private static final ClientImplementation DEFAULT_IMPLEMENTATION = ClientImplementation.AUTO;

    public enum ClientImplementation {
        AUTO,
        OKHTTP,
        SPRING
    }

    private ClientImplementation implementation = DEFAULT_IMPLEMENTATION;

    public ClientImplementation getImplementation() {
        return implementation;
    }

    public void setImplementation(ClientImplementation implementation) {
        this.implementation = implementation;
    }

}
