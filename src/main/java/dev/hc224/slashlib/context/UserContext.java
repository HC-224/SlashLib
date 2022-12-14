package dev.hc224.slashlib.context;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.UserInteractionEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import reactor.util.annotation.NonNull;
import reactor.util.annotation.Nullable;

import java.util.Optional;

/**
 * The base context provided to a User Command (context menu).
 * Created from the required data set by the command in a {@link UserContextBuilder}
 */
public class UserContext extends Context {
    private final @NonNull UserInteractionEvent event;

    private final @Nullable User targetUser;
    private final @Nullable Member targetMember;

    public UserContext(UserContextBuilder builder) {
        super(builder);

        this.event          = builder.getEvent();

        this.targetUser     = builder.getTargetUser();
        this.targetMember   = builder.getTargetMember();
    }

    /** @return the {@link ChatInputInteractionEvent} which corresponding to this interaction */
    @NonNull
    public UserInteractionEvent getEvent() {
        return event;
    }

    /** @return the {@link User} this interaction was called on *if requested* */
    @Nullable
    public Optional<User> getTargetUser() {
        return Optional.ofNullable(targetUser);
    }

    /** @return the {@link Member} this interaction was called on *if requested* */
    @Nullable
    public Optional<Member> getTargetMember() {
        return Optional.ofNullable(targetMember);
    }
}
