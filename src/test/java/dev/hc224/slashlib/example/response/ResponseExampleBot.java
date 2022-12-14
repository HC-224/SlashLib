package dev.hc224.slashlib.example.response;

import dev.hc224.slashlib.GenericSlashLib;
import dev.hc224.slashlib.GenericSlashLibBuilder;
import dev.hc224.slashlib.example.response.interactions.chat.Error;
import dev.hc224.slashlib.example.response.interactions.chat.ErrorReactor;
import dev.hc224.slashlib.example.response.interactions.chat.Ping;
import dev.hc224.slashlib.example.response.interactions.chat.UnknownState;
import dev.hc224.slashlib.example.response.interactions.message.MessageInfo;
import dev.hc224.slashlib.example.response.interactions.user.UserInfo;
import dev.hc224.slashlib.example.response.slashlib.ResponseEventReceiver;
import dev.hc224.slashlib.example.response.slashlib.context.*;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.EventDispatcher;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.Objects;

/**
 * An Example Bot which uses custom context classes and a custom event receiver to
 *  provide a post-execution state for commands to set during their execution with
 *  the {@link CommandResponse} class.
 *
 * It should be noted that this example doesn't have any custom context builders,
 *  as the {@link CommandResponse} class is always provided and only used during
 *  the command execution lifecycle.
 *
 * This can be considered a practical example, as a consistent post-execution state
 *  is useful for analytics (has a command started having increased execution issues?)
 *  and various other actions that are desired at the end of execution.
 *
 * This functionality would be in the base library, however for the sake of flexibility
 *  and project scope it is left to the end user to design themselves.
 */
public class ResponseExampleBot {
    private static final Logger logger = Loggers.getLogger(ResponseExampleBot.class);

    // The Discord Client
    private static DiscordClient discordClient;
    // The Gateway of our Discord client, also provided with every event/entity
    private static GatewayDiscordClient discordGateway;
    // The application ID of your bot, required to update interactions
    private static long applicationId;
    // The SlashLib instance, you *must* keep a reference to this if you want to access it
    private static GenericSlashLib<
            ResponseChatContext, ResponseChatContextBuilder,
            ResponseUserContext, ResponseUserContextBuilder, ResponseMessageContext, ResponseMessageContextBuilder
            >
        slashLib;

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
                ResponseChatContext, ResponseChatContextBuilder,
                ResponseUserContext, ResponseUserContextBuilder, ResponseMessageContext, ResponseMessageContextBuilder
                >
            slashLibBuilder = null;
        try {
            // Note, if arguments cannot be inferred, then the types of the generics and classes don't match.
            slashLibBuilder = new GenericSlashLibBuilder<>(
                    ResponseChatContext.class, ResponseChatContextBuilder.class,
                    ResponseUserContext.class, ResponseUserContextBuilder.class, ResponseMessageContext.class, ResponseMessageContextBuilder.class
            );
        } catch (NoSuchMethodException nsme) {
            logger.error("Couldn't get constructors for response classes, this may be a Java version error!");
            System.exit(-1);
        }

        // Set custom event receiver
        // A bit of a rough edge shows here, do not chain the builder
        //  pattern as the return type is a superclass.
        slashLibBuilder.setReceiver(ResponseEventReceiver::new);
        slashLibBuilder
            .addGlobalChatCommand(new Ping())
            .addGlobalChatCommand(new UnknownState())
            .addGlobalChatCommand(new Error())
            .addGlobalChatCommand(new ErrorReactor())
            .addGlobalUserCommand(new UserInfo())
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
    public static GenericSlashLib<
            ResponseChatContext, ResponseChatContextBuilder,
            ResponseUserContext, ResponseUserContextBuilder, ResponseMessageContext, ResponseMessageContextBuilder
            >
        getSlashLib()                                       { return slashLib; }
    public static GatewayDiscordClient getDiscordGateway()  { return discordGateway; }
    public static long getApplicationId()                   { return applicationId; }
}
