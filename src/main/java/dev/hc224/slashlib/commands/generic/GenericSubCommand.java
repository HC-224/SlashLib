package dev.hc224.slashlib.commands.generic;

import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.rest.util.Permission;

/**
 * A Class which represents a sub command at the second or third level.
 */
public abstract class GenericSubCommand<IC extends ChatContext, IB extends ChatContextBuilder> extends GenericChatCommand<IC, IB> {
    protected GenericSubCommand(String name, String description) {
        super(name, description, ApplicationCommandOption.Type.SUB_COMMAND);
    }

    /**
     * Throw an {@link IllegalStateException} when trying to set the default permission of a GenericSubCommand as it does nothing.
     * @throws IllegalStateException when called
     */
    @Override
    protected void setDefaultMemberPermissions(Permission... permissions) {
        throw new IllegalStateException("Member permissions can only be applied to GenericTopCommand or GenericTopGroupCommand! Command: " + this.getClass().getSimpleName());
    }
}
