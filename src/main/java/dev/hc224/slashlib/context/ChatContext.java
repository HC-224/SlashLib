package dev.hc224.slashlib.context;

import dev.hc224.slashlib.utility.OptionsList;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import reactor.util.annotation.NonNull;

/**
 * The base context provided to a Chat Command.
 * Created from the required data set by the command in a {@link ChatContextBuilder}
 */
public class ChatContext extends Context {
    private final @NonNull ChatInputInteractionEvent event;

    private final @NonNull ApplicationCommandInteraction aci;
    private final @NonNull OptionsList options;

    public ChatContext(ChatContextBuilder builder) {
        super(builder);

        this.event      = builder.getEvent();

        this.aci        = builder.getAci();
        this.options    = builder.getOptions();
    }

    /**
     * @return The {@link ChatInputInteractionEvent} of the interaction.
     */
    @NonNull
    public ChatInputInteractionEvent getEvent() {
        return event;
    }

    /**
     * @return The {@link ApplicationCommandInteraction} of the interaction.
     *         Unlike the event, this will always be present.
     */
    @NonNull
    public ApplicationCommandInteraction getAci() {
        return aci;
    }

    /**
     * @return the options provided by the calling user
     * */
    @NonNull
    public OptionsList getOptions() {
        return options;
    }
}
