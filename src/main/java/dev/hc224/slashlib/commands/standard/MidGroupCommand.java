package dev.hc224.slashlib.commands.standard;

import dev.hc224.slashlib.commands.generic.GenericMidGroupCommand;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;

/**
 * A wrapper class for {@link MidGroupCommand} to simplify the default/standard usages of SlashLib.
 */
public class MidGroupCommand extends GenericMidGroupCommand<ChatContext, ChatContextBuilder> {
    protected MidGroupCommand(String name, String description) {
        super(name, description);
    }
}
