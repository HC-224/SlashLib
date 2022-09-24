package dev.hc224.slashlib.example.response.slashlib.context;

import dev.hc224.slashlib.context.UserContext;
import dev.hc224.slashlib.context.UserContextBuilder;
import discord4j.core.event.domain.interaction.UserInteractionEvent;

/**
 * While there is nothing extra added to this builder, the build() method MUST
 *  be overridden to instantiate the correct context class ({@link ResponseChatContext}).
 */
public class ResponseUserContextBuilder extends UserContextBuilder {
    public ResponseUserContextBuilder(UserInteractionEvent event) {
        super(event);
    }

    // This must be overridden for all custom context classes
    @Override
    public UserContext build() {
        return new ResponseUserContext(this);
    }
}
