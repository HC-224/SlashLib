package dev.hc224.slashlib.example.guild.interactions.chat;

import dev.hc224.slashlib.commands.standard.TopCommand;
import dev.hc224.slashlib.context.ChatContext;
import reactor.core.publisher.Mono;

public class GuildPing extends TopCommand {
    public GuildPing() {
        super("ping", "get a pong! (guild)");
    }

    @Override
    public Mono<ChatContext> executeChat(ChatContext context) {
        return context.getEvent().reply("Guild Pong!").thenReturn(context);
    }
}
