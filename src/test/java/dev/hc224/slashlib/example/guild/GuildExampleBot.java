package dev.hc224.slashlib.example.guild;

import dev.hc224.slashlib.SlashLib;
import dev.hc224.slashlib.SlashLibBuilder;
import dev.hc224.slashlib.example.guild.interactions.chat.*;
import dev.hc224.slashlib.example.guild.interactions.message.MessageContentInfo;
import dev.hc224.slashlib.example.guild.interactions.message.MessageReactionInfo;
import dev.hc224.slashlib.example.guild.interactions.user.MemberInfo;
import dev.hc224.slashlib.example.guild.interactions.user.UserInfo;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.EventDispatcher;

import java.util.Objects;

/**
 * An Example bot which contains guild commands.
 */
public class GuildExampleBot {
    // The Discord Client
    private static DiscordClient discordClient;
    // The Gateway of our Discord client, also provided with every event/entity
    private static GatewayDiscordClient discordGateway;
    // The application ID of your bot, required to update interactions
    private static long applicationId;
    // The SlashLib instance, you *must* keep a reference to this if you want to access it
    private static SlashLib slashLib;

    /**
     * Entry point for the program, the token must be provided as an environment variable with `TOKEN=YOUR_TOKEN_HERE`
     */
    public static void main(String[] args) {
        // Create our Discord Client
        discordClient = DiscordClient.create(System.getenv("TOKEN"));
        // Create an EventDispatcher we will register SlashLib with
        EventDispatcher dispatcher = EventDispatcher.builder().build();

        // Setup SlashLib and add Interactions
        SlashLibBuilder slashLibBuilder = SlashLibBuilder.create()
            // Global Commands to Manage Guild Commands
            .addGlobalChatCommand(new TogglePing())
            .addGlobalChatCommand(new SyncGuildCommands())
            .addGlobalChatCommand(new DeleteGuildCommands())
            // Global Commands
            .addGlobalChatCommand(new GlobalPing())
            .addGlobalUserCommand(new UserInfo())
            .addGlobalMessageCommand(new MessageContentInfo())
            // Guild Commands
            .addGuildChatCommand(new GuildPing())
            .addGuildUserCommand(new MemberInfo())
            .addGuildMessageCommand(new MessageReactionInfo());

        // Set the guild command state provider.
        slashLibBuilder.setGuildCommandStateProvider(NonPersistentGuildCommandStateProvider.getInstance());

        // Build the SlashLib instance
        slashLib = slashLibBuilder.build();
        // Register the SlashLib instance with the event dispatcher to
        //  receive relevant interaction events
        slashLib.registerAsListener(dispatcher);

        // Login to Discord with our Event Dispatcher
        discordGateway = discordClient.gateway()
            .setEventDispatcher(dispatcher)
            .login()
            .block();

        // Get our bots application ID
        Long nullableApplicationId = discordClient.getApplicationId().block();
        Objects.requireNonNull(nullableApplicationId, "Couldn't get our application ID from Discord, please try again.");
        applicationId = nullableApplicationId;
        // Register Interactions with Discord
        slashLib.getCommandRegister().registerGlobalCommands(discordClient.getApplicationService(), applicationId);

        // Block the main thread until disconnect so the JVM doesn't exit.
        discordGateway.onDisconnect().block();
    }

    public static DiscordClient getDiscordClient()          { return discordClient; }
    public static SlashLib getSlashLib()                    { return slashLib; }
    public static GatewayDiscordClient getDiscordGateway()  { return discordGateway; }
    public static long getApplicationId()                   { return applicationId; }
}
