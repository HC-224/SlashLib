package dev.hc224.slashlib.example.guild.interactions.chat;

import dev.hc224.slashlib.commands.standard.TopCommand;
import dev.hc224.slashlib.context.ChatContext;
import reactor.core.publisher.Mono;

public class GlobalPing extends TopCommand {
    public GlobalPing() {
        super("ping", "get a pong! (global)");
    }

    @Override
    public Mono<ChatContext> executeChat(ChatContext context) {
        return context.getEvent().reply("Global Pong!").thenReturn(context);
    }
}
