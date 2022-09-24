package dev.hc224.slashlib.commands.generic;

import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;

/**
 * A class representing a top-level slash command.
 */
public abstract class GenericTopCommand<IC extends ChatContext, IB extends ChatContextBuilder> extends GenericChatCommand<IC, IB> {
    public GenericTopCommand(String name, String description) {
        super(name, description, null);
    }
}
