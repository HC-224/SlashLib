package dev.hc224.slashlib;

import dev.hc224.slashlib.context.*;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.event.domain.interaction.UserInteractionEvent;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.lang.reflect.Constructor;
import java.util.function.Function;

/**
 * The core logic and organization for SlashLib.
 *
 * @param <IC> The {@link Class} used for {@link ChatContext}
 * @param <IB> The {@link Class} used for {@link ChatContextBuilder}
 * @param <UC> The {@link Class} used for {@link UserContext}
 * @param <UB> The {@link Class} used for {@link UserContextBuilder}
 * @param <MC> The {@link Class} used for {@link MessageContext}
 * @param <MB> The {@link Class} used for {@link MessageContextBuilder}
 */
public class GenericSlashLib<
        IC extends ChatContext, IB extends ChatContextBuilder,
        UC extends UserContext, UB extends UserContextBuilder,
        MC extends MessageContext, MB extends MessageContextBuilder
        > {
    private static final Logger logger = Loggers.getLogger(GenericSlashLib.class);

    protected static boolean created = false;

    // The CommandRegister used to update/search for commands
    protected CommandRegister<IC, IB, UC, UB, MC, MB> commandRegister;
    // The event receiver for processing incoming interactions
    GenericEventReceiver<IC, UC, MC> receiver;

    // Chat Input
    final Class<IC> chatInputContextClass;
    final Class<IB> chatInputContextBuilderClass;
    final Constructor<IB> chatInputContextBuilderConstructor;
    // Message
    final Class<MC> messageContextClass;
    final Class<MB> messageContextBuilderClass;
    final Constructor<MB> messageContextBuilderConstructor;
    // User
    final Class<UC> userContextClass;
    final Class<UB> userContextBuilderClass;
    final Constructor<UB> userContextBuilderConstructor;

    protected GenericSlashLib(GenericSlashLibBuilder<IC, IB, UC, UB, MC, MB> builder) {
        this.commandRegister = null;
        this.receiver = null;

        this.chatInputContextClass = builder.chatInputContextClass;
        this.chatInputContextBuilderClass = builder.chatInputContextBuilderClass;
        this.chatInputContextBuilderConstructor = builder.chatInputContextBuilderConstructor;

        this.messageContextClass = builder.messageContextClass;
        this.messageContextBuilderClass = builder.messageContextBuilderClass;
        this.messageContextBuilderConstructor = builder.messageContextBuilderConstructor;

        this.userContextClass = builder.userContextClass;
        this.userContextBuilderClass = builder.userContextBuilderClass;
        this.userContextBuilderConstructor = builder.userContextBuilderConstructor;
    }

    /**
     * Create an instance of GenericSlashLib from a builder using defaults if not present.
     *
     * @param builder the {@link ChatContextBuilder} with custom values
     * @return a new instance of GenericSlashLib which can be used to process commands
     */
    static <
            IC extends ChatContext, IB extends ChatContextBuilder,
            UC extends UserContext, UB extends UserContextBuilder,
            MC extends MessageContext, MB extends MessageContextBuilder
            > GenericSlashLib<IC, IB, UC, UB, MC, MB>
            create(GenericSlashLibBuilder<IC, IB, UC, UB, MC, MB> builder) {
        if (created) {
            throw new IllegalStateException("GenericSlashLib already created!");
        }
        GenericSlashLib<IC, IB, UC, UB, MC, MB> genericSlashLib = new GenericSlashLib<>(builder);
        genericSlashLib.receiver = builder.eventReceiverProducer.apply(genericSlashLib);
        genericSlashLib.commandRegister = CommandRegister.create(
                builder.globalChatCommands,
                builder.globalUserCommands,
                builder.globalMessageCommands,
                builder.guildChatCommands,
                builder.guildUserCommands,
                builder.guildMessageCommands,
                builder.guildCommandStateProvider);
        created = true;
        return genericSlashLib;
    }

    /**
     * Register a listener for a Discord event with customized logging.
     *
     * @param eventDispatcher the event dispatcher to register with
     * @param event the event to listen for
     * @param mapper the method to call
     * @param <E> any Discord event
     * @param <T> any Publisher which accepts the event
     */
    private <E extends Event, T> void registerListener(EventDispatcher eventDispatcher, Class<E> event, Function<E, Publisher<T>> mapper) {
        eventDispatcher.on(event)
            .flatMap(e -> Flux.defer(() -> mapper.apply(e))
                .onErrorResume(t -> {
                    logger.error("Error while handling CHAT_INPUT event");
                    logger.error(t.getClass().getCanonicalName() + ": " + t.getMessage());
                    return Mono.empty();
                }))
            .subscribe();
    }

    /**
     * Register this instance with an {@link EventDispatcher} to handle:
     * {@link ChatInputInteractionEvent}
     * {@link UserInteractionEvent}
     * {@link MessageInteractionEvent}
     * {@link ChatInputAutoCompleteEvent}
     *
     * @param eventDispatcher the {@link EventDispatcher} to be used with the bots future {@link GatewayDiscordClient}
     */
    public void registerAsListener(EventDispatcher eventDispatcher) {
        // Should not be any notable performance overhead to register listeners for events that are never received
        registerListener(eventDispatcher, ChatInputInteractionEvent.class,  getReceiver()::receiveChatInputInteractionEvent);
        registerListener(eventDispatcher, UserInteractionEvent.class,       getReceiver()::receiveUserInteractionEvent);
        registerListener(eventDispatcher, MessageInteractionEvent.class,    getReceiver()::receiveMessageInteractionEvent);
        registerListener(eventDispatcher, ChatInputAutoCompleteEvent.class, getReceiver()::receiveAutoCompleteEvent);
    }

    /**
     * @return the {@link CommandRegister} used by this instance
     */
    public CommandRegister<IC, IB, UC, UB, MC, MB> getCommandRegister() { return commandRegister; }

    /**
     * @return the event receiver being used to handle interaction events
     */
    public GenericEventReceiver<IC, UC, MC> getReceiver() {
        return receiver;
    }

    /**
     * @return the class being used for CHAT_INPUT context
     */
    public Class<IC> getChatInputContextClass() {
        return chatInputContextClass;
    }

    /**
     * @return the class being used to build CHAT_INPUT contexts
     */
    public Class<IB> getChatInputContextBuilderClass() {
        return chatInputContextBuilderClass;
    }

    /**
     * @return the constructor of the CHAT_INPUT context builder
     */
    public Constructor<IB> getChatInputContextBuilderConstructor() {
        return chatInputContextBuilderConstructor;
    }

    /**
     * @return the class being used for MESSAGE context
     */
    public Class<MC> getMessageContextClass() {
        return messageContextClass;
    }

    /**
     * @return the class being used to build MESSAGE contexts
     */
    public Class<MB> getMessageContextBuilderClass() {
        return messageContextBuilderClass;
    }

    /**
     * @return the constructor of the MESSAGE context builder
     */
    public Constructor<MB> getMessageContextBuilderConstructor() {
        return messageContextBuilderConstructor;
    }

    /**
     * @return the class being used for USER context
     */
    public Class<UC> getUserContextClass() {
        return userContextClass;
    }

    /**
     * @return the class being used to build USER contexts
     */
    public Class<UB> getUserContextBuilderClass() {
        return userContextBuilderClass;
    }

    /**
     * @return the constructor of the USER context builder
     */
    public Constructor<UB> getUserContextBuilderConstructor() {
        return userContextBuilderConstructor;
    }
}
