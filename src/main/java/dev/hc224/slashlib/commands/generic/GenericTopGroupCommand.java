package dev.hc224.slashlib.commands.generic;

import dev.hc224.slashlib.commands.InvalidCommandLocationException;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;

/**
 * A class representing a Group Command at the Top level.
 *
 * @param <IC> the {@link ChatContext} class provided to commands for execution.
 * @param <IB> the {@link ChatContextBuilder} class provided to commands to set requested data.
 */
public abstract class GenericTopGroupCommand<IC extends ChatContext, IB extends ChatContextBuilder> extends GenericGroupCommand<IC, IB> {
    protected GenericTopGroupCommand(String name, String description) {
        super(name, description);
    }

    /**
     * Add a sub command to this group, can be a GenericSubCommand or GenericMidGroupCommand
     *
     * @param command the sub command to add to this group
     */
    @Override
    public void addSubCommand(GenericChatCommand<IC, IB> command) {
        if (!(command instanceof GenericSubCommand || command instanceof GenericMidGroupCommand)) {
            throw new InvalidCommandLocationException(command, this, "GenericSubCommand or GenericMidGroupCommand");
        }
        super.addSubCommand(command);
    }
}
