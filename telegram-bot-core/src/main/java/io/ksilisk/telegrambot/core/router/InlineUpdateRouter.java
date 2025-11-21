package io.ksilisk.telegrambot.core.router;

import com.pengrad.telegrambot.model.Update;
import io.ksilisk.telegrambot.core.handler.update.UpdateHandler;
import io.ksilisk.telegrambot.core.registry.rule.inline.InlineRuleRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class InlineUpdateRouter implements UpdateRouter {
    private static final Logger log = LoggerFactory.getLogger(InlineUpdateRouter.class);

    private final InlineRuleRegistry inlineRuleRegistry;

    public InlineUpdateRouter(InlineRuleRegistry inlineRuleRegistry) {
        this.inlineRuleRegistry = inlineRuleRegistry;
    }

    @Override
    public boolean supports(Update update) {
        return update.inlineQuery() != null;
    }

    @Override
    public boolean route(Update update) {
        if (!this.supports(update)) {
            log.warn("InlineUpdateRouter invoked with unsupported update (missing inlineQuery). \n" +
                    "Ensure that router.supports(update) is checked before calling route().");
            return false;
        }

        Optional<UpdateHandler> updateHandler = inlineRuleRegistry.find(update.inlineQuery());
        if (updateHandler.isEmpty()) {
            return false;
        }
        updateHandler.get().handle(update);
        return true;
    }
}
