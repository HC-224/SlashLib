package dev.hc224.slashlib.example.guild.interactions.chat;

import dev.hc224.slashlib.CommandRegister;
import dev.hc224.slashlib.commands.standard.TopCommand;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;
import dev.hc224.slashlib.example.guild.GuildExampleBot;
import discord4j.rest.service.ApplicationService;
import reactor.core.publisher.Mono;

/**
 * An example command that directly creates/deletes a guild command called "ping" which references {@link GuildPing}.
 * The update should be immediate, check the full list of commands in the command picker to see the changes.
 *
 * This bypasses the {@link CommandRegister} logic to test guild command registration.
 */
public class TogglePing extends TopCommand {
    public TogglePing() {
        super("toggle-ping", "add/remove the guild ping command for this guild");
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
            // Get the guild-specific ping command
            .filter(acd -> acd.name().equals("ping"))
            .next() // There will only be one, go back to being a mono
            // Delete it if present (note the `.thenReturn(1)` to avoid going empty
            .flatMap(acd -> applicationService.deleteGuildApplicationCommand(applicationId, guildId, Long.parseLong(acd.id())).thenReturn(1))
            // If not present, add it
            .switchIfEmpty(applicationService.createGuildApplicationCommand(
                applicationId,
                guildId,
                GuildExampleBot.getSlashLib()
                    .getCommandRegister()
                    .getCommandStructure()
                    .getGuildChatCommands()
                    .get("ping")
                    .asRequest())
                .thenReturn(0))
            // Send a response
            .flatMap(integer -> {
                if (integer == 0) {
                    return context.getEvent().reply("Created Ping Guild Command!");
                } else {
                    return context.getEvent().reply("Deleted Ping Guild Command!");
                }
            })
            .thenReturn(context);
    }

    @Override
    public ChatContextBuilder setRequestData(ChatContextBuilder builder) {
        return (ChatContextBuilder) builder.requireGuild();
    }
}
