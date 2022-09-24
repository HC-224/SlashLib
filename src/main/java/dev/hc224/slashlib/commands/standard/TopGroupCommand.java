package dev.hc224.slashlib.commands.standard;

import dev.hc224.slashlib.commands.generic.GenericTopGroupCommand;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;

/**
 * A wrapper class for {@link GenericTopGroupCommand} to simplify the default/standard usages of SlashLib.
 */
public class TopGroupCommand extends GenericTopGroupCommand<ChatContext, ChatContextBuilder> {
    protected TopGroupCommand(String name, String description) {
        super(name, description);
    }
}
