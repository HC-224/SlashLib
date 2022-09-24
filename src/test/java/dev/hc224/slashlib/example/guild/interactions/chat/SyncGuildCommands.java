package dev.hc224.slashlib.example.guild.interactions.chat;

import dev.hc224.slashlib.commands.standard.TopCommand;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;
import dev.hc224.slashlib.example.guild.GuildExampleBot;
import discord4j.rest.service.ApplicationService;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * A command that invokes SlashLib to update guild commands for all connected guilds.
 */
public class SyncGuildCommands extends TopCommand {
    public SyncGuildCommands() {
        super("sync-guild-commands", "update guild commands for all connected guilds");
    }

    @Override
    public Mono<ChatContext> executeChat(ChatContext context) {
        ApplicationService applicationService = GuildExampleBot.getDiscordClient().getApplicationService();
        long applicationId = GuildExampleBot.getApplicationId();

        // We've required the guild be present
        //noinspection OptionalGetWithoutIsPresent
        return Mono.just(context.getGuild())
            .map(guild -> GuildExampleBot.getSlashLib().getCommandRegister().registerGuildCommands(applicationService, applicationId, Collections.singletonList(guild.get().getId().asLong())))
            .flatMap(guildsUpdated -> context.getEvent().reply("Updated " + guildsUpdated + " guilds."))
            .thenReturn(context);
    }

    public ChatContextBuilder setRequestData(ChatContextBuilder builder) {
        return (ChatContextBuilder) builder.requireGuild();
    }
}
