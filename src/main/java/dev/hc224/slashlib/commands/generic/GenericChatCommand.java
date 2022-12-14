package dev.hc224.slashlib.commands.generic;

import dev.hc224.slashlib.commands.BaseCommand;
import dev.hc224.slashlib.context.AutoCompleteContext;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;
import discord4j.core.object.command.ApplicationCommand;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 * Generic superclass of all CHAT_INPUT commands.
 *
 * @param <IC> the {@link ChatContext} class provided to commands for execution.
 * @param <IB> the {@link ChatContextBuilder} class provided to commands to set requested data.
 */
public abstract class GenericChatCommand<IC extends ChatContext, IB extends ChatContextBuilder> extends BaseCommand {
    // null when GenericTopCommand: The type of chat input command this is
    private final ApplicationCommandOption.Type chatType;
    // null when no options: Options for the command
    private List<ApplicationCommandOptionData> options;

    // null when not GenericGroupCommand: SubCommands and Sub-GroupCommands
    protected final Map<String, GenericChatCommand<IC, IB>> subCommands;

    protected GenericChatCommand(String name,
                                 String description,
                                 @Nullable ApplicationCommandOption.Type type) {
        super(name, description, type, ApplicationCommand.Type.CHAT_INPUT);
        this.chatType = type;
        this.options = null;

        if (type == ApplicationCommandOption.Type.SUB_COMMAND_GROUP) {
            this.subCommands = new HashMap<>();
        } else {
            this.subCommands = null;
        }
    }

    public abstract Mono<IC> executeChat(IC context);

    /**
     * Set the required data for this interaction to be executed. By default, nothing is required.
     *
     * @param contextBuilder the context builder that will be used to create the interaction context.
     * @return the context builder modified
     */
    public IB setRequestData(IB contextBuilder) {
        return contextBuilder;
    }

    public Mono<Void> receiveAutoCompleteEvent(AutoCompleteContext context) {
        //noinspection OptionalGetWithoutIsPresent
        return Mono.error(new RuntimeException("No autocomplete implementation for command: " +
                context.getEvent().getInteraction().getCommandInteraction().get().getName()));
    }

    /**
     * @return a representative {@link ApplicationCommandOptionData} of this class to be used as option data
     */
    public ApplicationCommandOptionData asOptionData() {
        ImmutableApplicationCommandOptionData.Builder builder = ApplicationCommandOptionData.builder()
                .name(this.getName())
                .description(this.getDescription())
                .type(this.getChatType().getValue());
        List<ApplicationCommandOptionData> options = this.getOptions();
        if (options != null) {
            builder.options(options);
        }
        return builder.build();
    }

    /**
     * @return a representative {@link ApplicationCommandRequest} to compare/create this data with Discord
     */
    @Override
    public ApplicationCommandRequest asRequest() {
        // Get a builder with common values created and add the options for this command if present
        ImmutableApplicationCommandRequest.Builder builder = this.buildBaseRequest();
        if (this.getOptions() != null) {
            builder.addAllOptions(this.getOptions());
        }
        return builder.build();
    }

    /**
     * @param option an {@link ApplicationCommandOptionData} to add to this command
     */
    public void addOption(ApplicationCommandOptionData option) {
        if (this.options == null) this.options = new ArrayList<>();
        this.options.add(option);
    }

    /**
     * @param option an {@link ApplicationCommandOptionData} builder consumer to add to this command
     */
    public void addOption(Consumer<ImmutableApplicationCommandOptionData.Builder> option) {
        ImmutableApplicationCommandOptionData.Builder builder = ApplicationCommandOptionData.builder();
        option.accept(builder);
        this.addOption(builder.build());
    }

    /**
     * Get a sub command of this command, only searches one level
     *
     * @param name the name of the sub command to get
     * @return the sub command if present, null if missing
     */
    public GenericChatCommand<IC, IB> getSubCommand(String name) {
        Objects.requireNonNull(subCommands, "SubCommands field is null");
        return subCommands.getOrDefault(name, null);
    }

    public List<ApplicationCommandOptionData> getOptions() { return options; }
    public ApplicationCommandOption.Type getChatType() { return chatType; }
    public Map<String, GenericChatCommand<IC, IB>> getSubCommands() { return subCommands; }
}
