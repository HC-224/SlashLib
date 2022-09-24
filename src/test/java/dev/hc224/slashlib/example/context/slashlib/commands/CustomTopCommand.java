package dev.hc224.slashlib.example.context.slashlib.commands;

import dev.hc224.slashlib.commands.generic.GenericTopCommand;
import dev.hc224.slashlib.commands.standard.TopCommand;
import dev.hc224.slashlib.example.context.slashlib.context.CustomChatContext;
import dev.hc224.slashlib.example.context.slashlib.context.CustomChatContextBuilder;

/**
 * A customized {@link GenericTopCommand} that specifies custom context classes created by the end user.
 * Notice that {@link TopCommand} is almost the same.
 * This class effectively hides the types used for the context classes.
 */
public abstract class CustomTopCommand extends GenericTopCommand<CustomChatContext, CustomChatContextBuilder> {
    protected CustomTopCommand(String name, String description) {
        super(name, description);
    }
}
