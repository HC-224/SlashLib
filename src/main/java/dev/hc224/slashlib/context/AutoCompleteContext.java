package dev.hc224.slashlib.context;

import dev.hc224.slashlib.utility.OptionsList;
import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.entity.User;

import java.util.List;

/**
 * A simple class providing some autocomplete event context.
 */
public class AutoCompleteContext {
    private final ChatInputAutoCompleteEvent event;
    private final ApplicationCommandInteraction aci;
    private final OptionsList optionsList;
    private final User user;

    public AutoCompleteContext(ChatInputAutoCompleteEvent event, ApplicationCommandInteraction aci, List<ApplicationCommandInteractionOption> options) {
        this.event = event;
        this.aci = aci;
        this.optionsList = new OptionsList(options, event.getInteraction());
        this.user = event.getInteraction().getUser();
    }

    public ChatInputAutoCompleteEvent       getEvent()      { return event; }
    public ApplicationCommandInteraction    getAci()        { return aci; }
    public OptionsList                      getOptions()    { return optionsList; }
    public User                             getUser()       { return user; }
}
