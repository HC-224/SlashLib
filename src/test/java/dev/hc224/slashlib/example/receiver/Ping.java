package dev.hc224.slashlib.example.receiver;

import dev.hc224.slashlib.commands.standard.TopCommand;
import dev.hc224.slashlib.context.ChatContext;
import reactor.core.publisher.Mono;

/**
 * Example command, see the basic example package for information about command structure!
 */
public class Ping extends TopCommand {
    public Ping() {
        super("ping", "get a pong!");
    }

    @Override
    public Mono<ChatContext> executeChat(ChatContext context) {
        return context.getEvent().reply("Pong!").thenReturn(context);
    }
}
