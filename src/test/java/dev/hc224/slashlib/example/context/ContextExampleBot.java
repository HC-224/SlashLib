package dev.hc224.slashlib.example.context;

import dev.hc224.slashlib.GenericSlashLib;
import dev.hc224.slashlib.GenericSlashLibBuilder;
import dev.hc224.slashlib.context.MessageContext;
import dev.hc224.slashlib.context.MessageContextBuilder;
import dev.hc224.slashlib.context.UserContext;
import dev.hc224.slashlib.context.UserContextBuilder;
import dev.hc224.slashlib.example.context.interactions.chat.Ping;
import dev.hc224.slashlib.example.context.interactions.chat.TrueValue;
import dev.hc224.slashlib.example.context.slashlib.context.CustomChatContext;
import dev.hc224.slashlib.example.context.slashlib.context.CustomChatContextBuilder;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.EventDispatcher;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.Objects;

/**
 * An Example Bot which provides extra methods to require data in the command context.
 * While the example is only provided for chat input commands, the same process can be applied to
 *  message and user commands.
 *
 * This example peels away the "standard" classes and interacts with lowe level "Generic" classes.
 */
public class ContextExampleBot {
    private static final Logger logger = Loggers.getLogger(ContextExampleBot.class);

    // The Discord Client
    private static DiscordClient discordClient;
    // The Gateway of our Discord client, also provided with every event/entity
    private static GatewayDiscordClient discordGateway;
    // The application ID of your bot, required to update interactions
    private static long applicationId;
    // The SlashLib instance, you *must* keep a reference to this if you want to access it
    private static GenericSlashLib<
            // We specify our custom receivers here
            CustomChatContext, CustomChatContextBuilder,
            // We are still using the same user/message context classes
            UserContext, UserContextBuilder, MessageContext, MessageContextBuilder
            > slashLib;

    /**
     * Entry point for the program, the token must be provided as an environment variable with `TOKEN=YOUR_TOKEN_HERE`
     */
    public static void main(String[] args) {
        // Create our Discord Client
        discordClient = DiscordClient.create(System.getenv("TOKEN"));
        // Create an EventDispatcher we will register SlashLib with
        EventDispatcher dispatcher = EventDispatcher.builder().build();

        // Things get dirty here, the GenericSlashLibBuilder must be provided the classes
        //  of the context classes, it will find the constructors for each builder.
        // As a result, a `NoSuchMethod` exception can be thrown when obtaining the constructors.
        // Although, this should not happen if the classes are properly extended and the constructor
        //  is overloaded.

        // Setup SlashLib and add Interactions
        GenericSlashLibBuilder<
                CustomChatContext, CustomChatContextBuilder,
                UserContext, UserContextBuilder, MessageContext, MessageContextBuilder
                > slashLibBuilder = null;
        try {
             slashLibBuilder = new GenericSlashLibBuilder<>(
                // Provide our custom classes
                CustomChatContext.class, CustomChatContextBuilder.class,
                // Provided the default classes
                     UserContext.class, UserContextBuilder.class, MessageContext.class, MessageContextBuilder.class
             );
        } catch (NoSuchMethodException nsme) {
            logger.error("Couldn't get constructors for custom classes, this may be a Java version error!");
            System.exit(-1);
        }

        // From here on, things are much more normal.
        // Add Chat Input commands, the types will match for these.
        slashLibBuilder
            .addGlobalChatCommand(new Ping())
            .addGlobalChatCommand(new TrueValue());

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
    public static GenericSlashLib<
            CustomChatContext, CustomChatContextBuilder,
            UserContext, UserContextBuilder, MessageContext, MessageContextBuilder
            > getSlashLib()  { return slashLib; }
    public static GatewayDiscordClient getDiscordGateway()  { return discordGateway; }
    public static long getApplicationId()                   { return applicationId; }
}
