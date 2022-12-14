package dev.hc224.slashlib.context;

import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import reactor.util.annotation.NonNull;
import reactor.util.annotation.Nullable;

import java.util.Optional;

/**
 * The base context provided to a Message Command (context menu).
 * Created from the required data set by the command in a {@link MessageContextBuilder}
 */
public class MessageContext extends Context {
    private final @NonNull MessageInteractionEvent event;

    private final @Nullable Message targetMessage;
    private final @Nullable User messageAuthor;
    private final @Nullable Member messageAuthorAsMember;

    public MessageContext(MessageContextBuilder builder) {
        super(builder);

        this.event                  = builder.getEvent();

        this.targetMessage          = builder.getTargetMessage();
        this.messageAuthor          = builder.getMessageAuthor();
        this.messageAuthorAsMember  = builder.getMessageAuthorAsMember();
    }

    /**
     * @return the {@link MessageInteractionEvent} which corresponding to this interaction
     */
    public @NonNull MessageInteractionEvent getEvent() {
        return event;
    }

    /**
     * @return the {@link Message} this interaction was called on *if requested*
     */
    public Optional<Message> getTargetMessage() {
        return Optional.ofNullable(targetMessage);
    }

    /**
     * @return the {@link User} who authored the {@link Message} this interaction was called on *if requested*
     */
    public Optional<User> getMessageAuthor() {
        return Optional.ofNullable(messageAuthor);
    }

    /**
     * @return the {@link Member} who authored the {@link Message} this interaction was called on *if requested*
     */
    public Optional<Member> getMessageAuthorAsMember() {
        return Optional.ofNullable(messageAuthorAsMember);
    }
}
