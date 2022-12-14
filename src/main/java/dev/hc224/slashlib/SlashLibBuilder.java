package dev.hc224.slashlib;

import dev.hc224.slashlib.commands.standard.MessageCommand;
import dev.hc224.slashlib.commands.standard.TopCommand;
import dev.hc224.slashlib.commands.standard.TopGroupCommand;
import dev.hc224.slashlib.commands.standard.UserCommand;
import dev.hc224.slashlib.context.*;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.event.domain.interaction.UserInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * A user-friendly wrapper for {@link GenericSlashLib} which uses all standard wrapper classes for an easy and
 *  straightforward implementation.
 *
 * Can also be used as a reference for creating a custom wrapper for custom classes.
 */
public class SlashLibBuilder
        extends GenericSlashLibBuilder<
        ChatContext, ChatContextBuilder,
        UserContext, UserContextBuilder,
        MessageContext, MessageContextBuilder
        > {

    private SlashLibBuilder(Class<ChatContext> chatInputContextClass,
                            Class<ChatContextBuilder> chatInputContextBuilderClass,
                            Constructor<ChatContextBuilder> chatInputContextBuilderConstructor,
                            Class<UserContext> userContextClass,
                            Class<UserContextBuilder> userContextBuilderClass,
                            Constructor<UserContextBuilder> userContextBuilderConstructor,
                            Class<MessageContext> messageContextClass,
                            Class<MessageContextBuilder> messageContextBuilderClass,
                            Constructor<MessageContextBuilder> messageContextBuilderConstructor) {
        super(chatInputContextClass,
                chatInputContextBuilderClass,
                chatInputContextBuilderConstructor,
                userContextClass,
                userContextBuilderClass,
                userContextBuilderConstructor,
                messageContextClass,
                messageContextBuilderClass,
                messageContextBuilderConstructor
        );
    }

    /**
     * Create a new builder with class properties set.
     *
     * @return a new {@link SlashLibBuilder} which can be used to customize and build a {@link SlashLib} instance.
     */
    public static SlashLibBuilder create() {
        try {
            Constructor<ChatContextBuilder> chatInputContextBuilderConstructor = ChatContextBuilder.class
                .getConstructor(
                    ChatInputInteractionEvent.class,
                    ApplicationCommandInteraction.class,
                    List.class);
            Constructor<MessageContextBuilder> messageContextBuilderConstructor = MessageContextBuilder.class
                .getConstructor(MessageInteractionEvent.class);
            Constructor<UserContextBuilder> userContextBuilderConstructor = UserContextBuilder.class
                .getConstructor(UserInteractionEvent.class);

            return new SlashLibBuilder(
                ChatContext.class, ChatContextBuilder.class, chatInputContextBuilderConstructor,
                    UserContext.class, UserContextBuilder.class, userContextBuilderConstructor,
                    MessageContext.class, MessageContextBuilder.class, messageContextBuilderConstructor
            );
        } catch (NoSuchMethodException nsme) {
            // Should not occur under right conditions, as we know we have the correct setup to access the constructors
            throw new RuntimeException("No valid constructor for a builder found, this may be a Java version incompatibility.", nsme);
        }
    }

    /**
     * @param command a {@link TopCommand} to register and use globally.
     * @return this instance modified (with a class cast)
     */
    public SlashLibBuilder addGlobalChatCommand(TopCommand command) {
        return (SlashLibBuilder) super.addGlobalChatCommand(command);
    }

    /**
     * @param command a {@link TopGroupCommand} to register and use globally.
     * @return this instance modified (with a class cast)
     */
    public SlashLibBuilder addGlobalChatCommand(TopGroupCommand command) {
        return (SlashLibBuilder) super.addGlobalChatCommand(command);
    }

    /**
     * @param command a {@link UserCommand} to register and use globally.
     * @return this instance modified (with a class cast)
     */
    public SlashLibBuilder addGlobalUserCommand(UserCommand command) {
        return (SlashLibBuilder) super.addGlobalUserCommand(command);
    }

    /**
     * @param command a {@link MessageCommand} to register and use globally.
     * @return this instance modified (with a class cast)
     */
    public SlashLibBuilder addGlobalMessageCommand(MessageCommand command) {
        return (SlashLibBuilder) super.addGlobalMessageCommand(command);
    }

    /**
     * @param command a {@link TopCommand} to use for guild commands.
     * @return this instance modified (with a class cast)
     */
    public SlashLibBuilder addGuildChatCommand(TopCommand command) {
        return (SlashLibBuilder) super.addGuildChatCommand(command);
    }

    /**
     * @param command a {@link TopGroupCommand} to use for guild commands.
     * @return this instance modified (with a class cast)
     */
    public SlashLibBuilder addGuildChatCommand(TopGroupCommand command) {
        return (SlashLibBuilder) super.addGuildChatCommand(command);
    }

    /**
     * @param command a {@link UserCommand} to use for guild commands.
     * @return this instance modified (with a class cast)
     */
    public SlashLibBuilder addGuildUserCommand(UserCommand command) {
        return (SlashLibBuilder) super.addGuildUserCommand(command);
    }

    /**
     * @param command a {@link MessageCommand} to use for guild commands.
     * @return this instance modified (with a class cast)
     */
    public SlashLibBuilder addGuildMessageCommand(MessageCommand command) {
        return (SlashLibBuilder) super.addGuildMessageCommand(command);
    }

    /**
     * @return a new {@link SlashLib} created from the options set with this builder.
     */
    public SlashLib build() {
        return SlashLib.create(this);
    }
}
