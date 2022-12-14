package dev.hc224.slashlib;

import dev.hc224.slashlib.commands.generic.GenericChatCommand;
import dev.hc224.slashlib.commands.generic.GenericMessageCommand;
import dev.hc224.slashlib.commands.generic.GenericUserCommand;
import dev.hc224.slashlib.context.*;
import dev.hc224.slashlib.utility.CommandOptionPair;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.event.domain.interaction.UserInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A representation of the command structure used for slash commands.
 *
 * Some notes on my (HC-224) observations about slash commands
 * - Each "path" in the command tree must lead to a command.
 * -- This means if /command sub_command is a command then /command is not a callable command.
 * -- Same goes for /command group_command sub_command for command and group_command.
 * - The "name" of any type of command is just the name of that command.
 * -- /command group_command sub_command each have their own names and don't contain each other.
 * -- Discord seems to only navigate down, not up. See the next note section about options.
 * - The Group and Sub commands are options of the above command.
 * -- Knowing about the "path" and "name" means that if a command is not callable then it will
 *     have one option of type GROUP_COMMAND or SUB_COMMAND. We can assume that if there is 0 options,
 *     1 option that is not group/sub, or 2+ options of any type then the command is callable. This is key.
 *
 * With the above observations a tree is the optimal choice for organizing slash commands by name.
 * That is what this class exists to do.
 *
 * Where Discord slash commands exist as:
 * Root
 * - Command
 * - Command
 * -- GenericSubCommand
 * -- GenericSubCommand
 * -- GenericGroupCommand
 * --- GenericSubCommand
 *
 * The Tree will use classes as:
 * CommandStructure
 * - GenericTopCommand
 * - GenericTopGroupCommand
 * -- GenericSubCommand
 * -- GenericSubCommand
 * -- GenericMidGroupCommand
 * --- GenericSubCommand
 *
 * Commands need no knowledge of their parent or involvement in the tree.
 * Since only Commands and SubCommands are callable, GroupCommands are used for each uncallable command.
 *
 * User and Message context menu "commands" are kept in their own hashmap to avoid name collisions but are assembled
 *  as TopCommands when creating request data.
 *
 * Guild commands are all in their own hashmaps to prevent name collisions as well, global commands have a second
 *  hashmap with their ID as the key for fast determination of guild/global commands.
 */
public class CommandStructure<
        CI extends ChatContext, CB extends ChatContextBuilder,
        UC extends UserContext, UB extends UserContextBuilder,
        MC extends MessageContext, MB extends MessageContextBuilder
        > {
    private final Map<String, GenericChatCommand<CI, CB>> globalChatCommands;
    private final Map<String, GenericUserCommand<UC, UB>> globalUserCommands;
    private final Map<String, GenericMessageCommand<MC, MB>> globalMessageCommands;

    // Admittedly a bit redundant, but used to determine if a command is global
    private final Map<Snowflake, GenericChatCommand<CI, CB>> globalChatCommandsById;
    private final Map<Snowflake, GenericUserCommand<UC, UB>> globalUserCommandsById;
    private final Map<Snowflake, GenericMessageCommand<MC, MB>> globalMessageCommandsById;

    private final Map<String, GenericChatCommand<CI, CB>> guildChatCommands;
    private final Map<String, GenericUserCommand<UC, UB>> guildUserCommands;
    private final Map<String, GenericMessageCommand<MC, MB>> guildMessageCommands;

    /**
     * Create the Command Structure used for Slash Commands
     */
    public CommandStructure() {
        globalChatCommands = new HashMap<>();
        globalUserCommands = new HashMap<>();
        globalMessageCommands = new HashMap<>();

        globalChatCommandsById = new HashMap<>();
        globalUserCommandsById = new HashMap<>();
        globalMessageCommandsById = new HashMap<>();

        guildChatCommands = new HashMap<>();
        guildUserCommands = new HashMap<>();
        guildMessageCommands = new HashMap<>();
    }

    /**
     * Get a Chat Command for an incoming event. First attempts to get a global by ID then a guild by name.
     *
     * @param aci the command interaction from the event received
     * @return the {@link GenericUserCommand} that corresponds to the event
     */
    public CommandOptionPair<CI, CB> searchForChatCommand(ApplicationCommandInteraction aci) {
        // Attempt to get the command by its ID if it is a global command, if not then get the guild command.
        //noinspection OptionalGetWithoutIsPresent
        GenericChatCommand<CI, CB> command = this.globalChatCommandsById
                .getOrDefault(aci.getId().get(), this.guildChatCommands.get(aci.getName().get()));
        List<ApplicationCommandInteractionOption> options = aci.getOptions();
        // If there is only one option and it's a GenericSubCommand or SubCommandGroup option then search for it
        if (options.size() == 1 && options.get(0).getType().getValue() < 3) {
            return searchForChatCommand(command, options);
        } else {
            return new CommandOptionPair<>(command, options);
        }
    }

    /**
     * Search for a second or third level command recursively
     * @param command the command to check and search
     * @param options the options for this command
     * @return a callable command
     */
    private CommandOptionPair<CI, CB> searchForChatCommand(GenericChatCommand<CI, CB> command, List<ApplicationCommandInteractionOption> options) {
        // If there is only one option and it's a GenericSubCommand or SubCommandGroup option then search for it
        if (options.size() == 1 && options.get(0).getType().getValue() < 3) {
            ApplicationCommandInteractionOption option = options.get(0);
            return searchForChatCommand(command.getSubCommand(option.getName()), option.getOptions());
        } else {
            return new CommandOptionPair<>(command, options);
        }
    }

    /**
     * Get a User Context Command for an incoming event. First attempts to get a global by ID then a guild by name.
     *
     * @param event the event received
     * @return the {@link GenericUserCommand} that corresponds to the event
     */
    public GenericUserCommand<UC, UB> searchForUserCommand(UserInteractionEvent event) {
        return this.globalUserCommandsById.getOrDefault(event.getCommandId(), this.guildUserCommands.get(event.getCommandName()));
    }

    /**
     * Get a Message Context Command for an incoming event. First attempts to get a global by ID then a guild by name.
     *
     * @param event the event received
     * @return the {@link GenericUserCommand} that corresponds to the event
     */
    public GenericMessageCommand<MC, MB> searchForMessageCommand(MessageInteractionEvent event) {
        return this.globalMessageCommandsById.getOrDefault(event.getCommandId(), this.guildMessageCommands.get(event.getCommandName()));
    }

    /**
     * @param command a global chat command to add to the name hashmap.
     */
    void addGlobalChatCommand(GenericChatCommand<CI, CB> command) {
        globalChatCommands.put(command.getName(), command);
    }

    /**
     * @param command a global user command to add to the name hashmap.
     */
    void addGlobalUserCommand(GenericUserCommand<UC, UB> command) {
        globalUserCommands.put(command.getName(), command);
    }

    /**
     * @param command a global message command to add to the name hashmap.
     */
    void addGlobalMessageCommand(GenericMessageCommand<MC, MB> command) {
        globalMessageCommands.put(command.getName(), command);
    }

    /**
     * @param command a global chat command to add to the id hashmap.
     */
    void addGlobalChatCommand(GenericChatCommand<CI, CB> command, Snowflake id) {
        globalChatCommandsById.put(id, command);
    }

    /**
     * @param command a global user command to add to the id hashmap.
     */
    void addGlobalUserCommand(GenericUserCommand<UC, UB> command, Snowflake id) {
        globalUserCommandsById.put(id, command);
    }

    /**
     * @param command a global message command to add to the id hashmap.
     */
    void addGlobalMessageCommand(GenericMessageCommand<MC, MB> command, Snowflake id) {
        globalMessageCommandsById.put(id, command);
    }

    /**
     * @param command a guild chat command to add.
     */
    void addGuildChatCommand(GenericChatCommand<CI, CB> command) {
        guildChatCommands.put(command.getName(), command);
    }

    /**
     * @param command a guild user command to add.
     */
    void addGuildUserCommand(GenericUserCommand<UC, UB> command) {
        guildUserCommands.put(command.getName(), command);
    }

    /**
     * @param command a guild message command to add.
     */
    void addGuildMessageCommand(GenericMessageCommand<MC, MB> command) {
        guildMessageCommands.put(command.getName(), command);
    }

    public Map<String, GenericChatCommand<CI, CB>> getGlobalChatCommands() { return this.globalChatCommands; }
    public Map<String, GenericUserCommand<UC, UB>> getGlobalUserCommands() { return this.globalUserCommands; }
    public Map<String, GenericMessageCommand<MC, MB>> getGlobalMessageCommands() { return this.globalMessageCommands; }

    public Map<Snowflake, GenericChatCommand<CI, CB>> getGlobalChatCommandsById() { return this.globalChatCommandsById; }
    public Map<Snowflake, GenericUserCommand<UC, UB>> getGlobalUserCommandsById() { return this.globalUserCommandsById; }
    public Map<Snowflake, GenericMessageCommand<MC, MB>> getGlobalMessageCommandsById() { return this.globalMessageCommandsById; }

    public Map<String, GenericChatCommand<CI, CB>> getGuildChatCommands() { return this.guildChatCommands; }
    public Map<String, GenericUserCommand<UC, UB>> getGuildUserCommands() { return this.guildUserCommands; }
    public Map<String, GenericMessageCommand<MC, MB>> getGuildMessageCommands() { return this.guildMessageCommands; }
}
