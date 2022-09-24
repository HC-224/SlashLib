package dev.hc224.slashlib.commands.standard;

import dev.hc224.slashlib.commands.generic.GenericMessageCommand;
import dev.hc224.slashlib.context.MessageContext;
import dev.hc224.slashlib.context.MessageContextBuilder;

/**
 * A wrapper class for {@link GenericMessageCommand} to simplify the default/standard usages of SlashLib.
 */
public abstract class MessageCommand extends GenericMessageCommand<MessageContext, MessageContextBuilder> {
    protected MessageCommand(String name) {
        super(name);
    }
}
