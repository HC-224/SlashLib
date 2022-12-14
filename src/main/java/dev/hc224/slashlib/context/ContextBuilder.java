package dev.hc224.slashlib.context;

import discord4j.core.event.domain.interaction.InteractionCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TopLevelGuildChannel;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;
import reactor.util.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Core logic for all extending builder classes.
 *
 * Classes such as:
 * {@link discord4j.core.event.domain.interaction.ChatInputInteractionEvent}
 * {@link discord4j.core.event.domain.interaction.MessageInteractionEvent}
 * {@link discord4j.core.event.domain.interaction.UserInteractionEvent}
 * do not have a superclass which offers logic for getting the base fields in this class.
 * As such the subclasses that implement this class to handle events must provide the
 *  logic to access and set the fields in this class.
 *
 * It is a bit messy, but results in consistent access during the command lifecycle.
 */
public abstract class ContextBuilder {
    protected final @NonNull InteractionCreateEvent event;
    
    // List of Monos which will be zipped when building to gather all required data, throws an exception if empty
    protected final List<Mono<Integer>> requiredMonoList;
    // List of Monos which will be zipped when building to gather all optional data, doesn't throw an exception if empty
    protected final List<Mono<Integer>> requestMonoList;
    // If all requested data was retrieved successfully
    boolean allRequestedDataExists;

    @Nullable Guild guild;
    @Nullable MessageChannel messageChannel;
    @Nullable TopLevelGuildChannel topLevelGuildChannel;
    final @NonNull User user;
    @Nullable Member member;
    @Nullable User botUser;
    @Nullable Member botMember;

    /**
     * Create a new context builder, the user must be specified as all
     *  interaction events provide the user through D4J.
     *
     * @param event the event related to this context
     */
    ContextBuilder(@NonNull InteractionCreateEvent event) {
        this.event = event;
        
        this.requiredMonoList = new ArrayList<>();
        this.requestMonoList = new ArrayList<>();
        this.allRequestedDataExists = false;

        // Due to how collectData() works an error is thrown if Mono#zip() returns empty
        // This is because some methods of collecting data like requireGuildChannel() can go empty without error
        // So at least one entry must exist for commands that require no data
        this.requiredMonoList.add(Mono.just(1));
        // The same behavior is with the optional data, however to make an "all good" property available
        //  this cannot go empty either.
        this.requestMonoList.add(Mono.just(1));

        this.guild = null;
        this.messageChannel = null;
        this.topLevelGuildChannel = null;
        this.user = event.getInteraction().getUser();
        this.member = null;
        this.botUser = null;
        this.botMember = null;
    }

    /**
     * @return a new immutable {@link Context} from this builder.
     */
    public abstract Context build();

    /**
     * Collect the required and optional requested data for this context instance.
     *
     * @return this instance
     */
    public Mono<ContextBuilder> collectData() {
        // Collect required data, throwing an exception on failure
        Mono<Integer> requiredDataMonoZip = Mono.zip(requiredMonoList, (array) -> 1)
            .switchIfEmpty(Mono.error(new DataMissingException(this, "Couldn't collect all data!")));
        // Collect optional data, marking that not all optional data was retrieved on failure
        Mono<Integer> requestDataMonoZip = Mono.zip(requestMonoList, (array) -> 1)
            .doOnNext(_int -> allRequestedDataExists = true);

        return Mono.zip(requiredDataMonoZip, requestDataMonoZip)
            .thenReturn(this);
    }

    /**
     * Mark that the {@link Guild} the command was called in is required for command execution.
     * This method will collect the:
     * guild
     *
     * If the data cannot be collected then a {@link DataMissingException} will be thrown.
     * @return this instance
     */
    public ContextBuilder requireGuild() {
        getRequiredMonoList().add(getEvent().getInteraction().getGuild()
            .doOnNext(this::setGuild)
            .map(guild -> 1));
        return this;
    }

    /**
     * Mark that the {@link Guild} the command was called in is optionally used for command execution.
     * This method will collect the:
     * guild
     *
     * If the data cannot be collected then {@link Context#doesAllRequestedDataExist()} will return false.
     * @return this instance
     */
    public ContextBuilder requestGuild() {
        getRequestMonoList().add(getEvent().getInteraction().getGuild()
            .doOnNext(this::setGuild)
            .map(guild -> 1));
        return this;
    }

    /**
     * Mark that the {@link MessageChannel} the command was called in is required for command execution.
     * This method will collect the:
     * messageChannel
     *
     * If the data cannot be collected then a {@link DataMissingException} will be thrown.
     * @return this instance
     */
    public ContextBuilder requireMessageChannel() {
        getRequiredMonoList().add(getEvent().getInteraction().getChannel()
            .doOnNext(this::setMessageChannel)
            .map(messageChannel -> 1));
        return this;
    }

    /**
     * Mark that the {@link MessageChannel} the command was called in is optionally used for command execution.
     * This method will collect the:
     * messageChannel
     *
     * If the data cannot be collected then {@link Context#doesAllRequestedDataExist()} will return false.
     * @return this instance
     */
    public ContextBuilder requestMessageChannel() {
        getRequestMonoList().add(getEvent().getInteraction().getChannel()
            .doOnNext(this::setMessageChannel)
            .map(messageChannel -> 1));
        return this;
    }

    /**
     * Mark that the {@link GuildChannel} the command was called in is required for command execution.
     * This method will collect the:
     * messageChannel
     * guildChannel
     *
     * If the data cannot be collected then a {@link DataMissingException} will be thrown.
     * @return this instance
     */
    public ContextBuilder requireTopLevelGuildChannel() {
        getRequiredMonoList().add(getEvent().getInteraction().getChannel()
            .doOnNext(this::setMessageChannel)
            .ofType(TopLevelGuildChannel.class)
            .doOnNext(this::setTopLevelGuildChannel)
            .map(guildChannel -> 1));
        return this;
    }

    /**
     * Mark that the {@link GuildChannel} the command was called in is optionally used for command execution.
     * This method will collect the:
     * messageChannel
     * guildChannel
     *
     * If the data cannot be collected then {@link Context#doesAllRequestedDataExist()} will return false.
     * @return this instance
     */
    public ContextBuilder requestTopLevelGuildChannel() {
        getRequestMonoList().add(getEvent().getInteraction().getChannel()
            .doOnNext(this::setMessageChannel)
            .ofType(TopLevelGuildChannel.class)
            .doOnNext(this::setTopLevelGuildChannel)
            .map(guildChannel -> 1));
        return this;
    }

    /**
     * Mark that the {@link Member} the command was called by is required for command execution.
     * This method will collect the:
     * callingUserAsMember
     *
     * If the data cannot be collected then a {@link DataMissingException} will be thrown.
     * @return this instance
     */
    public ContextBuilder requireMember() {
        getRequiredMonoList().add(Mono.justOrEmpty(getEvent().getInteraction().getMember())
            .doOnNext(this::setMember)
            .map(member -> 1));
        return this;
    }

    /**
     * Mark that the {@link Member} the command was called by is optionally used for command execution.
     * This method will collect the:
     * callingUserAsMember
     *
     * If the data cannot be collected then {@link Context#doesAllRequestedDataExist()} will return false.
     * @return this instance
     */
    public ContextBuilder requestMember() {
        getRequestMonoList().add(Mono.justOrEmpty(getEvent().getInteraction().getMember())
            .doOnNext(this::setMember)
            .map(member -> 1));
        return this;
    }

    /**
     * Mark that the {@link User} for the bot is required for command execution.
     * This method will collect the:
     * botUser
     *
     * If the data cannot be collected then a {@link DataMissingException} will be thrown.
     * @return this instance
     */
    public ContextBuilder requireBotUser() {
        getRequiredMonoList().add(getEvent().getClient().getSelf()
            .doOnNext(this::setBotUser)
            .map(user -> 1));
        return this;
    }

    /**
     * Mark that the {@link User} for the bot is optionally used for command execution.
     * This method will collect the:
     * botUser
     *
     * If the data cannot be collected then {@link Context#doesAllRequestedDataExist()} will return false.
     * @return this instance
     */
    public ContextBuilder requestBotUser() {
        getRequestMonoList().add(getEvent().getClient().getSelf()
            .doOnNext(this::setBotUser)
            .map(user -> 1));
        return this;
    }

    /**
     * Mark that the {@link Member} for the bot is required for command execution.
     * This method will collect the:
     * botMember
     * guild
     *
     * If the data cannot be collected then a {@link DataMissingException} will be thrown.
     * @return this instance
     */
    public ContextBuilder requireBotMember() {
        getRequiredMonoList().add(getEvent().getInteraction().getGuild()
            .doOnNext(this::setGuild)
            .flatMap(guild -> guild.getMemberById(getEvent().getClient().getSelfId()))
            .doOnNext(this::setBotMember)
            .map(member -> 1));
        return this;
    }

    /**
     * Mark that the {@link Member} for the bot is optionally used for command execution.
     * This method will collect the:
     * botMember
     * guild
     *
     * If the data cannot be collected then a {@link DataMissingException} will be thrown.
     * @return this instance
     */
    public ContextBuilder requestBotMember() {
        getRequestMonoList().add(getEvent().getInteraction().getGuild()
            .doOnNext(this::setGuild)
            .flatMap(guild -> guild.getMemberById(getEvent().getClient().getSelfId()))
            .doOnNext(this::setBotMember)
            .map(member -> 1));
        return this;
    }

    /**
     * Only to be called by custom context classes.
     *
     * @return the {@link InteractionCreateEvent} which invoked this interaction
     */
    @NonNull
    public InteractionCreateEvent getEvent() {
        return event;
    }

    /**
     * Only to be called by custom context classes.
     *
     * @return the {@link List} of {@link Mono}s which must provide a value for the interaction to execute
     */
    public List<Mono<Integer>> getRequiredMonoList() {
        return requiredMonoList;
    }

    /**
     * Only to be called by custom context classes.
     *
     * @return the {@link List} of {@link Mono}s which may provide a value before the interaction executes
     */
    public List<Mono<Integer>> getRequestMonoList() {
        return requestMonoList;
    }

    /**
     * Only to be called by custom context classes.
     * 
     * @return true if all requested optional data is present
     */
    public boolean isAllRequestedDataExists() {
        return allRequestedDataExists;
    }

    /**
     * Only to be called by custom context classes.
     * 
     * @return the {@link Guild} in which the interaction was invoked
     */
    @Nullable
    public Guild getGuild() {
        return guild;
    }

    /**
     * Only to be called by custom context classes.
     *
     * @return the {@link MessageChannel} in which the interaction was invoked
     */
    @Nullable
    public MessageChannel getMessageChannel() {
        return messageChannel;
    }

    /**
     * Only to be called by custom context classes.
     *
     * @return the {@link TopLevelGuildChannel} in which the interaction was invoked
     */
    @Nullable
    public TopLevelGuildChannel getTopLevelGuildChannel() {
        return topLevelGuildChannel;
    }

    /**
     * Only to be called by custom context classes.
     *
     * @return the {@link User} which invoked the interaction
     */
    @NonNull
    public User getUser() {
        return user;
    }

    /**
     * Only to be called by custom context classes.
     *
     * @return the {@link Member} which invoked the interaction
     */
    @Nullable
    public Member getMember() {
        return member;
    }

    /**
     * Only to be called by custom context classes.
     *
     * @return the {@link User} for this bot
     */
    @Nullable
    public User getBotUser() {
        return botUser;
    }

    /**
     * Only to be called by custom context classes.
     *
     * @return the {@link Member} for this bot
     */
    @Nullable
    public Member getBotMember() {
        return botMember;
    }

    protected void setGuild(@NonNull Guild guild) {
        this.guild = guild;
    }

    protected void setMessageChannel(@NonNull MessageChannel messageChannel) {
        this.messageChannel = messageChannel;
    }

    protected void setTopLevelGuildChannel(@NonNull TopLevelGuildChannel topLevelGuildChannel) {
        this.topLevelGuildChannel = topLevelGuildChannel;
    }

    protected void setMember(@NonNull Member member) {
        this.member = member;
    }

    protected void setBotUser(@NonNull User botUser) {
        this.botUser = botUser;
    }

    protected void setBotMember(@NonNull Member botMember) {
        this.botMember = botMember;
    }
}
