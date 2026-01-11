package io.ksilisk.telegrambot.autoconfigure.config.transport;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import io.ksilisk.telegrambot.core.ingress.NoIngressUpdateIngress;
import io.ksilisk.telegrambot.core.ingress.UpdateIngress;

class NoIngressModeConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(NoIngressModeConfiguration.class));

    @Test
    void shouldCreateDisabledUpdateIngressWhenModeIsDisabled() {
        contextRunner.withPropertyValues("telegram.bot.mode=NO_INGRESS")
                .run(context -> {
                    assertThat(context).hasSingleBean(UpdateIngress.class);
                    assertThat(context).hasSingleBean(NoIngressUpdateIngress.class);
                });
    }

    @Test
    void shouldNotCreateBeanWhenModeIsNotDisabled() {
        contextRunner.withPropertyValues("telegram.bot.mode=LONG_POLLING")
                .run(context -> assertThat(context).doesNotHaveBean(NoIngressUpdateIngress.class));
    }
}
