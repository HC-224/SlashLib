package dev.hc224.slashlib.example.response.slashlib.context;

import dev.hc224.slashlib.context.MessageContext;
import dev.hc224.slashlib.context.MessageContextBuilder;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;

/**
 * While there is nothing extra added to this builder, the build() method MUST
 *  be overridden to instantiate the correct context class ({@link ResponseChatContext}).
 */
public class ResponseMessageContextBuilder extends MessageContextBuilder {
    public ResponseMessageContextBuilder(MessageInteractionEvent event) {
        super(event);
    }

    // This must be overridden for all custom context classes
    @Override
    public MessageContext build() {
        return new ResponseMessageContext(this);
    }
}
