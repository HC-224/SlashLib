package dev.hc224.slashlib.commands.generic;

import dev.hc224.slashlib.commands.MissingSubCommandException;
import dev.hc224.slashlib.context.AutoCompleteContext;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Class representing a Group Command at any level.
 * This class should not be instantiated directly, use {@link GenericTopGroupCommand} or {@link GenericMidGroupCommand} instead.
 */
public abstract class GenericGroupCommand<IC extends ChatContext, IB extends ChatContextBuilder> extends GenericChatCommand<IC, IB> {
    protected GenericGroupCommand(String name, String description) {
        super(name, description, ApplicationCommandOption.Type.SUB_COMMAND_GROUP);
    }

    /**
     * Add a (sub)command to this group
     * Type should be checked before calling this from {@link GenericTopGroupCommand}
     * or {@link GenericMidGroupCommand}
     *
     * @param command the sub command to add to this group
     */
    protected void addSubCommand(GenericChatCommand<IC, IB> command) {
        Objects.requireNonNull(this.subCommands);
        this.subCommands.put(command.getName(), command);
    }

    /**
     * @throws IllegalStateException GroupCommands cannot be called through Discord. Calling this is an error.
     */
    @Override
    @Deprecated
    public Mono<IC> executeChat(IC context) {
        throw new IllegalStateException("GenericGroupCommand execute was invoked.");
    }

    /**
     * @throws IllegalStateException GroupCommands cannot be called through Discord. Calling this is an error.
     */
    @Override
    @Deprecated
    public IB setRequestData(IB builder) {
        throw new IllegalStateException("GenericGroupCommand setRequestData was invoked.");
    }

    /**
     * @throws IllegalStateException GroupCommands cannot be called through Discord. Calling this is an error.
     */
    @Override
    @Deprecated
    public Mono<Void> receiveAutoCompleteEvent(AutoCompleteContext event) {
        throw new IllegalStateException("GenericGroupCommand receiveAutocompleteEvent was invoked.");
    }

    /**
     * Create a representative {@link ApplicationCommandOptionData} of this class to be used as option data.
     * This also adds the sub commands as options.
     *
     * @return a representative {@link ApplicationCommandOptionData} of this class to be used as option data
     */
    @Override
    public List<ApplicationCommandOptionData> getOptions() {
        Objects.requireNonNull(this.subCommands);
        if (this.subCommands.size() == 0) {
            throw new MissingSubCommandException(this);
        }
        List<ApplicationCommandOptionData> options = new ArrayList<>();
        for (GenericChatCommand<IC, IB> command : this.subCommands.values()) {
            options.add(command.asOptionData());
        }
        return options;
    }
}
