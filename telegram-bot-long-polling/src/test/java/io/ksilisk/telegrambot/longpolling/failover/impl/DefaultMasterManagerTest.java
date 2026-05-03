package io.ksilisk.telegrambot.longpolling.failover.impl;

import io.ksilisk.telegrambot.core.executor.resolver.SwitchableTelegramBotApiUrlProvider;
import io.ksilisk.telegrambot.longpolling.failover.MasterSwitchPolicy;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class DefaultMasterManagerTest {

    @Test
    void shouldRecordSuccessInSwitchPolicy() {
        MasterSwitchPolicy switchPolicy = mock(MasterSwitchPolicy.class);
        SwitchableTelegramBotApiUrlProvider apiUrlProvider = mock(SwitchableTelegramBotApiUrlProvider.class);

        DefaultMasterManager manager = new DefaultMasterManager(switchPolicy, apiUrlProvider);

        manager.recordSuccess();

        verify(switchPolicy).recordSuccess();
        verifyNoMoreInteractions(apiUrlProvider);
    }

    @Test
    void shouldRecordFailureWithoutSwitchingWhenPolicyDoesNotRequireSwitch() {
        MasterSwitchPolicy switchPolicy = mock(MasterSwitchPolicy.class);
        SwitchableTelegramBotApiUrlProvider apiUrlProvider = mock(SwitchableTelegramBotApiUrlProvider.class);

        RuntimeException exception = new RuntimeException("connection failed");

        when(switchPolicy.shouldSwitch()).thenReturn(false);

        DefaultMasterManager manager = new DefaultMasterManager(switchPolicy, apiUrlProvider);

        manager.recordFailure(exception);

        verify(switchPolicy).recordFailure(exception);
        verify(switchPolicy).shouldSwitch();
        verify(switchPolicy, never()).reset();
        verify(apiUrlProvider, never()).switchToNext();
    }

    @Test
    void shouldSwitchToNextEndpointAndResetPolicyWhenPolicyRequiresSwitch() {
        MasterSwitchPolicy switchPolicy = mock(MasterSwitchPolicy.class);
        SwitchableTelegramBotApiUrlProvider apiUrlProvider = mock(SwitchableTelegramBotApiUrlProvider.class);

        RuntimeException exception = new RuntimeException("connection failed");

        when(switchPolicy.shouldSwitch()).thenReturn(true);

        DefaultMasterManager manager = new DefaultMasterManager(switchPolicy, apiUrlProvider);

        manager.recordFailure(exception);

        verify(switchPolicy).recordFailure(exception);
        verify(switchPolicy).shouldSwitch();
        verify(apiUrlProvider).switchToNext();
        verify(switchPolicy).reset();
    }
}
