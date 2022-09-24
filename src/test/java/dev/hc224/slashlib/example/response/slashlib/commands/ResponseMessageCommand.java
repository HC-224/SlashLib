package dev.hc224.slashlib.example.response.slashlib.commands;

import dev.hc224.slashlib.commands.generic.GenericMessageCommand;
import dev.hc224.slashlib.example.response.slashlib.context.ResponseMessageContext;
import dev.hc224.slashlib.example.response.slashlib.context.ResponseMessageContextBuilder;

/**
 * Like in the Context example, we extend the GenericXXXCommand classes to abstract out the types for our commands.
 */
public abstract class ResponseMessageCommand extends GenericMessageCommand<ResponseMessageContext, ResponseMessageContextBuilder> {
    public ResponseMessageCommand(String name) {
        super(name);
    }
}
