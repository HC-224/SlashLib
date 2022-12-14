package dev.hc224.slashlib;

import dev.hc224.slashlib.commands.InvalidCommandLocationException;
import dev.hc224.slashlib.commands.generic.*;
import dev.hc224.slashlib.context.*;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.event.domain.interaction.UserInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A builder for {@link GenericSlashLib} to customize the components of it.
 */
public class GenericSlashLibBuilder<
        IC extends ChatContext, IB extends ChatContextBuilder,
        UC extends UserContext, UB extends UserContextBuilder,
        MC extends MessageContext, MB extends MessageContextBuilder
        > {
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

    // Interaction Handling
    Function<GenericSlashLib<IC, IB, UC, UB, MC, MB>, GenericEventReceiver<IC, UC, MC>> eventReceiverProducer;
    // Global Commands
    List<GenericChatCommand<IC, IB>> globalChatCommands;
    List<GenericUserCommand<UC, UB>> globalUserCommands;
    List<GenericMessageCommand<MC, MB>> globalMessageCommands;
    // Guild Commands
    List<GenericChatCommand<IC, IB>> guildChatCommands;
    List<GenericUserCommand<UC, UB>> guildUserCommands;
    List<GenericMessageCommand<MC, MB>> guildMessageCommands;
    GuildCommandStateProvider guildCommandStateProvider;

    public GenericSlashLibBuilder(Class<IC> chatInputContextClass,
                                  Class<IB> chatInputContextBuilderClass,
                                  Class<UC> userContextClass,
                                  Class<UB> userContextBuilderClass,
                                  Class<MC> messageContextClass,
                                  Class<MB> messageContextBuilderClass)
            // Bad Bad Bad Bad Bad Bad Bad Bad
            throws NoSuchMethodException {

        // Chat Input
        this.chatInputContextClass = chatInputContextClass;
        this.chatInputContextBuilderClass = chatInputContextBuilderClass;
        this.chatInputContextBuilderConstructor = chatInputContextBuilderClass.getConstructor(
                ChatInputInteractionEvent.class,
                ApplicationCommandInteraction.class,
                List.class);

        // Message
        this.messageContextClass = messageContextClass;
        this.messageContextBuilderClass = messageContextBuilderClass;
        this.messageContextBuilderConstructor = messageContextBuilderClass.getConstructor(MessageInteractionEvent.class);

        // User
        this.userContextClass = userContextClass;
        this.userContextBuilderClass = userContextBuilderClass;
        this.userContextBuilderConstructor = userContextBuilderClass.getConstructor(UserInteractionEvent.class);

        // Interaction Handling
        this.eventReceiverProducer = GenericEventReceiverImpl::new; // Default receiver

        this.globalChatCommands = new ArrayList<>();
        this.globalUserCommands = new ArrayList<>();
        this.globalMessageCommands = new ArrayList<>();

        this.guildChatCommands = new ArrayList<>();
        this.guildUserCommands = new ArrayList<>();
        this.guildMessageCommands = new ArrayList<>();

        this.guildCommandStateProvider = new NoGuildCommandStateProvider();
    }

    protected GenericSlashLibBuilder(Class<IC> chatInputContextClass,
                                     Class<IB> chatInputContextBuilderClass,
                                     Constructor<IB> chatInputContextBuilderConstructor,
                                     Class<UC> userContextClass,
                                     Class<UB> userContextBuilderClass,
                                     Constructor<UB> userContextBuilderConstructor,
                                     Class<MC> messageContextClass,
                                     Class<MB> messageContextBuilderClass,
                                     Constructor<MB> messageContextBuilderConstructor) {
        // Chat Input
        this.chatInputContextClass = chatInputContextClass;
        this.chatInputContextBuilderClass = chatInputContextBuilderClass;
        this.chatInputContextBuilderConstructor = chatInputContextBuilderConstructor;

        // User
        this.userContextClass = userContextClass;
        this.userContextBuilderClass = userContextBuilderClass;
        this.userContextBuilderConstructor = userContextBuilderConstructor;

        // Message
        this.messageContextClass = messageContextClass;
        this.messageContextBuilderClass = messageContextBuilderClass;
        this.messageContextBuilderConstructor = messageContextBuilderConstructor;

        // Interaction Handling
        this.eventReceiverProducer = GenericEventReceiverImpl::new; // Default receiver

        this.globalChatCommands = new ArrayList<>();
        this.globalUserCommands = new ArrayList<>();
        this.globalMessageCommands = new ArrayList<>();

        this.guildChatCommands = new ArrayList<>();
        this.guildUserCommands = new ArrayList<>();
        this.guildMessageCommands = new ArrayList<>();

        this.guildCommandStateProvider = new NoGuildCommandStateProvider();
    }

    /**
     * Internal logic for adding guild/global commands which checks the class being added.
     *
     * @param command the command to add
     * @param function a function which adds the command to the guild/global command list
     * @return this instance modified
     */
    private GenericSlashLibBuilder<IC, IB, UC, UB, MC, MB>
            addChatCommand(GenericChatCommand<IC, IB> command, Function<GenericChatCommand<IC, IB>, Boolean> function) {
        if (!(command instanceof GenericTopCommand || command instanceof GenericTopGroupCommand)) {
            throw new InvalidCommandLocationException(command);
        }
        function.apply(command);
        return this;
    }

    /**
     * Add a chat input command to be registered *globally* when building a new {@link GenericSlashLib}.
     * An assertion is done to verify the command is an instance of {@link GenericTopCommand} or {@link GenericGroupCommand}
     *
     * @param command the command to add to the {@link CommandRegister}
     * @return this instance
     */
    public GenericSlashLibBuilder<IC, IB, UC, UB, MC, MB> addGlobalChatCommand(GenericChatCommand<IC, IB> command) {
        return addChatCommand(command, (bc) -> globalChatCommands.add(bc));
    }

    /**
     * Add a chat input command which will not be registered with Discord but will receive events when used in a *guild*.
     * An assertion is done to verify the command is an instance of {@link GenericTopCommand} or {@link GenericGroupCommand}
     *
     * @param command the command to add to the {@link CommandRegister}
     * @return this instance
     */
    public GenericSlashLibBuilder<IC, IB, UC, UB, MC, MB> addGuildChatCommand(GenericChatCommand<IC, IB> command) {
        return addChatCommand(command, (bc) -> guildChatCommands.add(bc));
    }

    /**
     * Add a user command to be registered *globally* when building a new {@link GenericSlashLib}.
     *
     * @param command the command to add to the {@link CommandRegister}
     * @return this instance
     */
    public GenericSlashLibBuilder<IC, IB, UC, UB, MC, MB> addGlobalUserCommand(GenericUserCommand<UC, UB> command) {
        globalUserCommands.add(command);
        return this;
    }

    /**
     * Add a user command which will not be registered with Discord but will receive events when used in a *guild*.
     *
     * @param command the command to add to the {@link CommandRegister}
     * @return this instance
     */
    public GenericSlashLibBuilder<IC, IB, UC, UB, MC, MB> addGuildUserCommand(GenericUserCommand<UC, UB> command) {
        guildUserCommands.add(command);
        return this;
    }

    /**
     * Add a message command to be registered *globally* when building a new {@link GenericSlashLib}.
     *
     * @param command the command to add to the {@link CommandRegister}
     * @return this instance
     */
    public GenericSlashLibBuilder<IC, IB, UC, UB, MC, MB> addGlobalMessageCommand(GenericMessageCommand<MC, MB> command) {
        globalMessageCommands.add(command);
        return this;
    }

    /**
     * Add a message command which will not be registered with Discord but will receive events when used in a *guild*.
     *
     * @param command the command to add to the {@link CommandRegister}
     * @return this instance
     */
    public GenericSlashLibBuilder<IC, IB, UC, UB, MC, MB> addGuildMessageCommand(GenericMessageCommand<MC, MB> command) {
        guildMessageCommands.add(command);
        return this;
    }

    /**
     * Provide the method of creating a new interaction event receiver, which is only used once.
     * The receiver should use the same generics as this class for IC, MC, and UC
     *
     * @param eventReceiverProducer the method called to produce a new interaction event receiver
     * @return this instance
     */
    public GenericSlashLibBuilder<IC, IB, UC, UB, MC, MB> setReceiver(Function<GenericSlashLib<IC, IB, UC, UB, MC, MB>, GenericEventReceiver<IC, UC, MC>> eventReceiverProducer) {
         this.eventReceiverProducer = eventReceiverProducer;
         return this;
    }

    /**
     * Provide the methods of determining if a command should exist for a guild.
     *
     * @param guildCommandStateProvider the {@link GuildCommandStateProvider} to retrieve guild command states from during registration/validation.
     * @return this instance
     */
    public GenericSlashLibBuilder<IC, IB, UC, UB, MC, MB> setGuildCommandStateProvider(GuildCommandStateProvider guildCommandStateProvider) {
        this.guildCommandStateProvider = guildCommandStateProvider;
        return this;
    }

    /**
     * Create a new {@link GenericSlashLib} with the set values overriding the defaults.
     * @return a created {@link GenericSlashLib} instance from this builder
     */
    public GenericSlashLib<IC, IB, UC, UB, MC, MB> build() {
        return GenericSlashLib.create(this);
    }
}
