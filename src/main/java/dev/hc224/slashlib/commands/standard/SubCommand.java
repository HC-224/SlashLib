package dev.hc224.slashlib.commands.standard;

import dev.hc224.slashlib.commands.generic.GenericSubCommand;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;

/**
 * A wrapper class for {@link GenericSubCommand} to simplify the default/standard usages of SlashLib.
 */
public abstract class SubCommand extends GenericSubCommand<ChatContext, ChatContextBuilder> {
    protected SubCommand(String name, String description) {
        super(name, description);
    }
}
