package dev.hc224.slashlib.context;

import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

/**
 * A builder class provided to user commands before the command logic is called.
 */
public class MessageContextBuilder extends ContextBuilder {
    protected final @NonNull MessageInteractionEvent event;

    Message targetMessage;
    User messageAuthor;
    Member messageAuthorAsMember;

    public MessageContextBuilder(@NonNull MessageInteractionEvent event) {
        super(event);

        this.event                  = event;

        this.targetMessage          = null;
        this.messageAuthor          = null;
        this.messageAuthorAsMember  = null;
    }

    @Override
    public MessageContext build() {
        return new MessageContext(this);
    }

    /**
     * Mark that the {@link Message} the command was called on is required for command execution.
     * This method will collect the:
     * message
     *
     * If the data cannot be collected then a {@link DataMissingException} will be thrown.
     * @return this instance
     */
    public MessageContextBuilder requireMessage() {
        getRequiredMonoList().add(getEvent().getTargetMessage()
            .doOnNext(this::setTargetMessage)
            .map(member -> 1));
        return this;
    }

    /**
     * Mark that the {@link Message} the command was called on is optionally used for command execution.
     * This method will collect the
     * message
     *
     * If the data cannot be collected then {@link Context#doesAllRequestedDataExist()} will return false.
     * @return this instance
     */
    public MessageContextBuilder requestMessage() {
        getRequestMonoList().add(getEvent().getTargetMessage()
            .doOnNext(this::setTargetMessage)
            .map(member -> 1));
        return this;
    }

    /**
     * Mark that the {@link Member} who authored the message this command
     *  was called on is required for command execution.
     * This method will collect the:
     * message
     * messageAuthor
     *
     * If the data cannot be collected then a {@link DataMissingException} will be thrown.
     * @return this instance
     */
    public MessageContextBuilder requireMessageAuthor() {
        getRequiredMonoList().add(getEvent().getTargetMessage()
            .doOnNext(this::setTargetMessage)
            .flatMap(message -> Mono.justOrEmpty(message.getAuthor()))
            .doOnNext(this::setMessageAuthor)
            .map(member -> 1));
        return this;
    }

    /**
     * Mark that the {@link Member} who authored the message this command
     *  was called on is optionally used for command execution.
     * This method will collect the:
     * message
     * messageAuthor
     *
     * If the data cannot be collected then {@link Context#doesAllRequestedDataExist()} will return false.
     * @return this instance
     */
    public MessageContextBuilder requestMessageAuthor() {
        getRequestMonoList().add(getEvent().getTargetMessage()
            .doOnNext(this::setTargetMessage)
            .flatMap(message -> Mono.justOrEmpty(message.getAuthor()))
            .doOnNext(this::setMessageAuthor)
            .map(member -> 1));
        return this;
    }

    /**
     * Mark that the {@link Member} who authored the message this command
     *  was called on is required for command execution.
     * This method will collect the:
     * guild
     * message
     * messageAuthor
     * messageAuthorAsMember
     *
     * If the data cannot be collected then a {@link DataMissingException} will be thrown.
     * @return this instance
     */
    public MessageContextBuilder requireMessageAuthorAsMember() {
        // As of D4J v3.2.0 message#getAuthorAsMember() will get the guild
        getRequiredMonoList().add(getEvent().getTargetMessage()
            .doOnNext(this::setTargetMessage)
            .flatMap(message -> message.getGuild()
                .doOnNext(this::setGuild)
                .flatMap(guild -> Mono.justOrEmpty(message.getAuthor())
                    .doOnNext(this::setMessageAuthor)
                    .flatMap(author -> guild.getMemberById(author.getId()))
                    .doOnNext(this::setMessageAuthorAsMember)))
            .map(member -> 1));
        return this;
    }

    /**
     * Mark that the {@link Member} who authored the message this command
     *  was called on is optionally used for command execution.
     * This method will collect the:
     * guild
     * message
     * messageAuthor
     * messageAuthorAsMember
     *
     * If the data cannot be collected then a {@link DataMissingException} will be thrown.
     * @return this instance
     */
    public MessageContextBuilder requestMessageAuthorAsMember() {
        // As of D4J v3.2.0 message#getAuthorAsMember() will get the guild
        getRequestMonoList().add(getEvent().getTargetMessage()
            .doOnNext(this::setTargetMessage)
            .flatMap(message -> message.getGuild()
                .doOnNext(this::setGuild)
                .flatMap(guild -> Mono.justOrEmpty(message.getAuthor())
                    .doOnNext(this::setMessageAuthor)
                    .flatMap(author -> guild.getMemberById(author.getId()))
                    .doOnNext(this::setMessageAuthorAsMember)))
            .map(member -> 1));
        return this;
    }

    /**
     * Only to be called by custom context classes.
     */
    @Override
    @NonNull
    public MessageInteractionEvent getEvent() {
        return event;
    }

    /**
     * Only to be called by custom context classes.
     *
     * @return the {@link Message} this interaction was called on
     */
    public Message getTargetMessage() {
        return targetMessage;
    }

    /**
     * Only to be called by custom context classes.
     *
     * @return the {@link User} of the author for the message this interaction was called on
     */
    public User getMessageAuthor() {
        return messageAuthor;
    }

    /**
     * Only to be called by custom context classes.
     *
     * @return the {@link Member} of the author for the message this interaction was called on
     */
    public Member getMessageAuthorAsMember() {
        return messageAuthorAsMember;
    }

    protected void setTargetMessage(@NonNull Message targetMessage) {
        this.targetMessage = targetMessage;
    }

    protected void setMessageAuthor(@NonNull User messageAuthor) {
        this.messageAuthor = messageAuthor;
    }

    protected void setMessageAuthorAsMember(@NonNull Member messageAuthorAsMember) {
        this.messageAuthorAsMember = messageAuthorAsMember;
    }
}
