package dev.hc224.slashlib.example.basic;

import dev.hc224.slashlib.SlashLib;
import dev.hc224.slashlib.SlashLibBuilder;
import dev.hc224.slashlib.example.basic.interactions.chat.*;
import dev.hc224.slashlib.example.basic.interactions.chat.choices.ChoicesGroup;
import dev.hc224.slashlib.example.basic.interactions.chat.info.InfoGroup;
import dev.hc224.slashlib.example.basic.interactions.message.MessageInfo;
import dev.hc224.slashlib.example.basic.interactions.user.UserInfo;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.EventDispatcher;

import java.util.Objects;

/**
 * A Basic Example Bot which has one of each available interaction type.
 */
public class BasicExampleBot {
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
            // Add Chat Input commands
            .addGlobalChatCommand(new Ping())
            .addGlobalChatCommand(new Echo())
            .addGlobalChatCommand(new About())
            .addGlobalChatCommand(new TestBan())
            .addGlobalChatCommand(new TestBanEnforce())
            .addGlobalChatCommand(new InfoGroup())
            .addGlobalChatCommand(new ChoicesGroup())
            // Add User commands
            .addGlobalUserCommand(new UserInfo())
            // Add Message commands
            .addGlobalMessageCommand(new MessageInfo());

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
