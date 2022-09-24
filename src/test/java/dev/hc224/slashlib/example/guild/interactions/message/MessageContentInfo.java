package dev.hc224.slashlib.example.guild.interactions.message;

import dev.hc224.slashlib.commands.standard.MessageCommand;
import dev.hc224.slashlib.context.MessageContext;
import dev.hc224.slashlib.context.MessageContextBuilder;
import reactor.core.publisher.Mono;

public class MessageContentInfo extends MessageCommand {
    public MessageContentInfo() {
        super("Info - Content");
    }

    @Override
    public Mono<MessageContext> executeMessage(MessageContext context) {
        // We've required the target message be present
        //noinspection OptionalGetWithoutIsPresent
        return context.getEvent()
            .reply("Message Content Length: " + context.getTargetMessage().get().getContent().length())
            .thenReturn(context);
    }

    @Override
    public MessageContextBuilder setRequestData(MessageContextBuilder builder) {
        return builder.requireMessage();
    }
}
