package dev.hc224.slashlib.example.guild.interactions.message;

import dev.hc224.slashlib.commands.standard.MessageCommand;
import dev.hc224.slashlib.context.MessageContext;
import dev.hc224.slashlib.context.MessageContextBuilder;
import reactor.core.publisher.Mono;

public class MessageReactionInfo extends MessageCommand {
    public MessageReactionInfo() {
        super("Info - Reactions");
    }

    @Override
    public Mono<MessageContext> executeMessage(MessageContext context) {
        // We've required the target message be present
        //noinspection OptionalGetWithoutIsPresent
        return context.getEvent()
            .reply("Message Unique Reactions: " + context.getTargetMessage().get().getReactions().size())
            .thenReturn(context);
    }

    @Override
    public MessageContextBuilder setRequestData(MessageContextBuilder builder) {
        return builder.requireMessage();
    }
}
