package io.ksilisk.telegrambot.core.properties;

import io.ksilisk.telegrambot.core.router.CallbackUpdateRouter;
import io.ksilisk.telegrambot.core.router.InlineUpdateRouter;
import io.ksilisk.telegrambot.core.router.MessageUpdateRouter;
import jakarta.validation.constraints.NotNull;

/**
 * Configuration properties controlling which routing modules are enabled.
 *
 * <p>Each routing group (message, inline, callback) can be individually
 * enabled or disabled. When disabled, the corresponding router and registry
 * are not created and related handlers are ignored.</p>
 */
public class RoutingProperties {
    @NotNull
    private MessageRoutingProperties message = new MessageRoutingProperties();

    @NotNull
    private InlineRoutingProperties inline = new InlineRoutingProperties();

    @NotNull
    private CallbackRoutingProperties callback = new CallbackRoutingProperties();

    /**
     * Controls whether message routing is enabled.
     *
     * <p>When disabled, message-based handlers and rules are not registered
     * and the {@link MessageUpdateRouter} is not created.</p>
     */
    public static class MessageRoutingProperties {
        private static final boolean DEFAULT_ENABLED = true;

        /**
         * Whether message routing is enabled.
         */
        private boolean enabled = DEFAULT_ENABLED;

        public boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * Controls whether inline query routing is enabled.
     *
     * <p>When disabled, inline handlers and rules are not registered
     * and the {@link InlineUpdateRouter} is not created.</p>
     */
    public static class InlineRoutingProperties {
        private static final boolean DEFAULT_ENABLED = true;

        /**
         * Whether message routing is enabled.
         */
        private boolean enabled = DEFAULT_ENABLED;

        public boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * Controls whether callback query routing is enabled.
     *
     * <p>When disabled, callback handlers are not registered
     * and the {@link CallbackUpdateRouter} is not created.</p>
     */
    public static class CallbackRoutingProperties {
        private static final boolean DEFAULT_ENABLED = true;

        /**
         * Whether callback query routing is enabled.
         */
        private boolean enabled = DEFAULT_ENABLED;

        public boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public MessageRoutingProperties getMessage() {
        return message;
    }

    public void setMessage(MessageRoutingProperties message) {
        this.message = message;
    }

    public InlineRoutingProperties getInline() {
        return inline;
    }

    public void setInline(InlineRoutingProperties inline) {
        this.inline = inline;
    }

    public CallbackRoutingProperties getCallback() {
        return callback;
    }

    public void setCallback(CallbackRoutingProperties callback) {
        this.callback = callback;
    }
}
