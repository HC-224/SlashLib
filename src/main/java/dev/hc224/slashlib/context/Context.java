package dev.hc224.slashlib.context;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TopLevelGuildChannel;
import reactor.util.annotation.NonNull;
import reactor.util.annotation.Nullable;

import java.util.Optional;

/**
 * Superclass for context provided during user logic part of interaction lifecycle.
 */
public abstract class Context {
    protected final @Nullable Guild guild;
    protected final @Nullable MessageChannel messageChannel;
    protected final @Nullable TopLevelGuildChannel topLevelGuildChannel;
    protected final @NonNull  User user;
    protected final @Nullable Member member;
    protected final @Nullable User botUser;
    protected final @Nullable Member botMember;

    protected final boolean allRequestedDataExists;

    Context(ContextBuilder builder) {
        this.guild                  = builder.getGuild();
        this.messageChannel         = builder.getMessageChannel();
        this.topLevelGuildChannel   = builder.getTopLevelGuildChannel();
        this.user                   = builder.getUser();
        this.member                 = builder.getMember();
        this.botUser                = builder.getBotUser();
        this.botMember              = builder.getBotMember();

        this.allRequestedDataExists = builder.isAllRequestedDataExists();
    }

    /**
     * @return The {@link Guild} this interaction was called in.
     */
    public final Optional<Guild> getGuild() { return Optional.ofNullable(guild); }

    /**
     * @return The {@link MessageChannel} this interaction was called in.
     */
    public final Optional<MessageChannel> getMessageChannel() { return Optional.ofNullable(messageChannel); }

    /**
     * @return The {@link TopLevelGuildChannel} this interaction was called in.
     */
    public final Optional<TopLevelGuildChannel> getTopLevelGuildChannel() { return Optional.ofNullable(topLevelGuildChannel); }

    /**
     * @return The {@link User} who called this interaction.
     *         Always present, provided by the event.
     */
    @NonNull
    public final User getUser() { return user; }

    /**
     * @return The {@link Member} for the {@link Context#user} who called this interaction.
     */
    public final Optional<Member> getMember() { return Optional.ofNullable(member); }

    /**
     * @return The {@link User} of the bot.
     */
    public final Optional<User> getBotUser() {
        return Optional.ofNullable(botUser);
    }

    /**
     * @return The {@link Member} of the bot for the {@link Guild} this command was called in.
     */
    public final Optional<Member> getBotMember() {
        return Optional.ofNullable(botMember);
    }

    /**
     * @return true if all data requested by the command was retrieved successfully
     */
    public final boolean doesAllRequestedDataExist() { return allRequestedDataExists; }
}
