package dev.hc224.slashlib.example.guild.interactions.chat;

import dev.hc224.slashlib.commands.standard.TopCommand;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;
import dev.hc224.slashlib.example.guild.GuildExampleBot;
import discord4j.rest.service.ApplicationService;
import reactor.core.publisher.Mono;

/**
 * An example command that directly deletes all guild commands on the guild it's called in.
 * Primarily used for cleaning up after testing.
 */
public class DeleteGuildCommands extends TopCommand {
    public DeleteGuildCommands() {
        super("delete-guild-cmds", "delete all guild commands for this guild");
    }

    @Override
    public Mono<ChatContext> executeChat(ChatContext context) {
        ApplicationService applicationService = GuildExampleBot.getDiscordClient().getApplicationService();
        long applicationId = GuildExampleBot.getApplicationId();
        // We've required the guild be present
        //noinspection OptionalGetWithoutIsPresent
        long guildId = context.getGuild().get().getId().asLong();

        return applicationService
            .getGuildApplicationCommands(GuildExampleBot.getApplicationId(), guildId)
            .flatMap(acd -> applicationService.deleteGuildApplicationCommand(applicationId, guildId, Long.parseLong(acd.id())).thenReturn(1))
            .count()
            .flatMap(deletedCount -> context.getEvent().reply("Deleted " + deletedCount + " Guild Commands."))
            .thenReturn(context);
    }

    @Override
    public ChatContextBuilder setRequestData(ChatContextBuilder builder) {
        return (ChatContextBuilder) builder.requireGuild();
    }
}
