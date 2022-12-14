package dev.hc224.slashlib.context;

import discord4j.core.event.domain.interaction.UserInteractionEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import reactor.util.annotation.NonNull;
import reactor.util.annotation.Nullable;

/**
 * A builder class provided to user commands before the command logic is called.
 */
public class UserContextBuilder extends ContextBuilder {
    protected final @NonNull UserInteractionEvent event;

    @Nullable User targetUser;
    @Nullable Member targetMember;

    public UserContextBuilder(@NonNull UserInteractionEvent event) {
        super(event);

        this.event          = event;

        this.targetUser     = null;
        this.targetMember   = null;
    }

    @Override
    public UserContext build() {
        return new UserContext(this);
    }

    /**
     * Mark that the {@link User} the interaction is called on is required for command execution.
     * This method will collect the:
     * targetUser
     *
     * If the data cannot be collected then a {@link DataMissingException} will be thrown.
     * @return this instance
     */
    public UserContextBuilder requireTargetUser() {
        getRequiredMonoList().add(getEvent().getTargetUser()
            .doOnNext(this::setTargetUser)
            .map(member -> 1));
        return this;
    }

    /**
     * Mark that the {@link User} the interaction is called on is optionally used for command execution.
     * This method will collect the:
     * targetUser
     *
     * If the data cannot be collected then {@link Context#doesAllRequestedDataExist()} will return false.
     * @return this instance
     */
    public UserContextBuilder requestTargetUser() {
        getRequestMonoList().add(getEvent().getTargetUser()
            .doOnNext(this::setTargetUser)
            .map(member -> 1));
        return this;
    }

    /**
     * Mark that the {@link Member} the interaction is called on is required for command execution.
     * This method will collect the:
     * guild
     * targetUser
     * targetMember
     *
     * If the data cannot be collected then a {@link DataMissingException} will be thrown.
     * @return this instance
     */
    public UserContextBuilder requireTargetMember() {
        getRequiredMonoList().add(getEvent().getTargetUser()
            .doOnNext(this::setTargetUser)
            .flatMap(user -> getEvent().getInteraction().getGuild()
                .doOnNext(this::setGuild)
                .flatMap(guild -> guild.getMemberById(user.getId()))
                .doOnNext(this::setTargetMember))
            .map(member -> 1));
        return this;
    }
    
    /**
     * Mark that the {@link Member} the interaction is called on is optionally used for command execution.
     * This method will collect the:
     * guild
     * targetUser
     * targetMember
     *
     * If the data cannot be collected then {@link Context#doesAllRequestedDataExist()} will return false.
     * @return this instance
     */
    public UserContextBuilder requestTargetMember() {
        getRequestMonoList().add(getEvent().getTargetUser()
            .doOnNext(this::setTargetUser)
            .flatMap(user -> getEvent().getInteraction().getGuild()
                .doOnNext(this::setGuild)
                .flatMap(guild -> guild.getMemberById(user.getId()))
                .doOnNext(this::setTargetMember))
            .map(member -> 1));
        return this;
    }

    /**
     * Only to be called by custom context classes.
     */
    @Override
    @NonNull
    public UserInteractionEvent getEvent() {
        return event;
    }

    /**
     * Only to be called by custom context classes.
     *
     * @return the {@link User} this interaction was called on
     */
    @Nullable
    public User getTargetUser() {
        return targetUser;
    }

    /**
     * Only to be called by custom context classes.
     *
     * @return the {@link Member} this interaction was called on
     */
    @Nullable
    public Member getTargetMember() {
        return targetMember;
    }

    protected void setTargetUser(@NonNull User targetUser) {
        this.targetUser = targetUser;
    }

    protected void setTargetMember(@NonNull Member targetMember) {
        this.targetMember = targetMember;
    }
}
