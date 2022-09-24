package dev.hc224.slashlib.example.response.slashlib.context;

import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;

import java.util.List;

/**
 * While there is nothing extra added to this builder, the build() method MUST
 *  be overridden to instantiate the correct context class ({@link ResponseChatContext}).
 */
public class ResponseChatContextBuilder extends ChatContextBuilder {
    public ResponseChatContextBuilder(ChatInputInteractionEvent event, ApplicationCommandInteraction aci, List<ApplicationCommandInteractionOption> options) {
        super(event, aci, options);
    }

    // This must be overridden for all custom context classes
    @Override
    public ChatContext build() {
        return new ResponseChatContext(this);
    }
}
