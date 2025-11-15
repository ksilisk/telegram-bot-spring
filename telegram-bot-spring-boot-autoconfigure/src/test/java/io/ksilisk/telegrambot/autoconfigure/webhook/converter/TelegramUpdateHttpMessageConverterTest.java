package io.ksilisk.telegrambot.autoconfigure.webhook.converter;

import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpInputMessage;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TelegramUpdateHttpMessageConverterTest {
    private final TelegramUpdateHttpMessageConverter converter = new TelegramUpdateHttpMessageConverter();

    @Test
    void shouldSupportUpdateClass() {
        assertThat(converter.supports(Update.class)).isTrue();
    }

    @Test
    void shouldNotSupportOtherClasses() {
        assertThat(converter.supports(String.class)).isFalse();
    }

    @Test
    void shouldExposeOnlyJsonMediaType() {
        List<MediaType> types = converter.getSupportedMediaTypes();
        assertThat(types).containsExactly(MediaType.APPLICATION_JSON);
    }

    @Test
    void shouldReadUpdateFromJson() throws Exception {
        String json = """
                {
                  "update_id":10000,
                  "message":{
                    "message_id":1365,
                    "text":"/start",
                    "chat": {
                      "id": 1111111,
                      "type": "private"
                    }
                  }
                }
                """;

        MockHttpInputMessage input = new MockHttpInputMessage(json.getBytes(StandardCharsets.UTF_8));

        Update update = converter.readInternal(Update.class, input);

        assertThat(update).isNotNull();
        assertThat(update.updateId()).isEqualTo(10000);
        assertThat(update.message()).isNotNull();
        assertThat(update.message().text()).isEqualTo("/start");
        assertThat(update.message().chat()).isNotNull();
        assertThat(update.message().chat().id()).isEqualTo(1111111L);
    }
}
