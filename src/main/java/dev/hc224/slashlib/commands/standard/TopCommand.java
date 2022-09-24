package dev.hc224.slashlib.commands.standard;

import dev.hc224.slashlib.commands.generic.GenericTopCommand;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;

/**
 * A wrapper class for {@link GenericTopCommand} to simplify the default/standard usages of SlashLib.
 */
public abstract class TopCommand extends GenericTopCommand<ChatContext, ChatContextBuilder> {
    protected TopCommand(String name, String description) {
        super(name, description);
    }
}
