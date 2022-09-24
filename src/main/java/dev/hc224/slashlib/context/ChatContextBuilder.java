package dev.hc224.slashlib.context;

import dev.hc224.slashlib.utility.OptionsList;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import reactor.util.annotation.NonNull;

import java.util.List;

/**
 * A builder class provided to chat input commands before the command logic is called.
 */
public class ChatContextBuilder extends ContextBuilder {
    protected final @NonNull ChatInputInteractionEvent event;
    protected final @NonNull ApplicationCommandInteraction aci;
    protected final @NonNull OptionsList options;

    public ChatContextBuilder(@NonNull ChatInputInteractionEvent event,
                              @NonNull ApplicationCommandInteraction aci,
                              @NonNull List<ApplicationCommandInteractionOption> options) {
        super(event);

        this.event      = event;
        this.aci        = aci;
        this.options    = new OptionsList(options, event.getInteraction());
    }

    @Override
    public ChatContext build() {
        return new ChatContext(this);
    }

    /**
     * Only to be called by custom context classes.
     *
     * @return the {@link ChatInputInteractionEvent} invoking this interaction
     */
    @Override
    @NonNull
    public ChatInputInteractionEvent getEvent() {
        return event;
    }

    /**
     * Only to be called by custom context classes.
     *
     * @return the {@link ApplicationCommandInteraction} invoking this interaction
     */
    @NonNull
    public ApplicationCommandInteraction getAci() {
        return aci;
    }

    /**
     * Only to be called by custom context classes.
     *
     * @return the {@link OptionsList} for this interaction
     */
    @NonNull
    public OptionsList getOptions() {
        return options;
    }
}
