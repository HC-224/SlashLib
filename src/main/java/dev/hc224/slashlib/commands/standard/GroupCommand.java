package dev.hc224.slashlib.commands.standard;

import dev.hc224.slashlib.commands.generic.GenericChatCommand;
import dev.hc224.slashlib.commands.generic.GenericGroupCommand;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;

/**
 * A wrapper class for {@link GenericChatCommand} to simplify the default/standard usages of SlashLib.
 * This class should not be instantiated directly, use {@link TopGroupCommand} or {@link MidGroupCommand} instead.
 */
public class GroupCommand extends GenericGroupCommand<ChatContext, ChatContextBuilder> {
    protected GroupCommand(String name, String description) {
        super(name, description);
    }
}
