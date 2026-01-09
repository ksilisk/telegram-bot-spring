package io.ksilisk.telegrambot.autoconfigure.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import io.ksilisk.telegrambot.core.ingress.DisabledUpdateIngress;
import io.ksilisk.telegrambot.core.ingress.UpdateIngress;

class TelegramBotDisabledAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(TelegramBotDisabledAutoConfiguration.class));

    @Test
    void shouldCreateDisabledUpdateIngressWhenModeIsDisabled() {
        contextRunner.withPropertyValues("telegram.bot.mode=DISABLED")
                .run(context -> {
                    assertThat(context).hasSingleBean(UpdateIngress.class);
                    assertThat(context).hasSingleBean(DisabledUpdateIngress.class);
                });
    }

    @Test
    void shouldNotCreateBeanWhenModeIsNotDisabled() {
        contextRunner.withPropertyValues("telegram.bot.mode=LONG_POLLING")
                .run(context -> assertThat(context).doesNotHaveBean(DisabledUpdateIngress.class));
    }
}