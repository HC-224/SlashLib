package dev.hc224.slashlib.example.response.slashlib.commands;

import dev.hc224.slashlib.commands.generic.GenericTopCommand;
import dev.hc224.slashlib.example.response.slashlib.context.ResponseChatContext;
import dev.hc224.slashlib.example.response.slashlib.context.ResponseChatContextBuilder;

/**
 * Like in the Context example, we extend the GenericXXXCommand classes to abstract out the types for our commands.
 */
public abstract class ResponseTopCommand extends GenericTopCommand<ResponseChatContext, ResponseChatContextBuilder> {
    public ResponseTopCommand(String name, String description) {
        super(name, description);
    }
}
