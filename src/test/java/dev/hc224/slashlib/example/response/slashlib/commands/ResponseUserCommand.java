package dev.hc224.slashlib.example.response.slashlib.commands;

import dev.hc224.slashlib.commands.generic.GenericUserCommand;
import dev.hc224.slashlib.example.response.slashlib.context.ResponseUserContext;
import dev.hc224.slashlib.example.response.slashlib.context.ResponseUserContextBuilder;

/**
 * Like in the Context example, we extend the GenericXXXCommand classes to abstract out the types for our commands.
 */
public abstract class ResponseUserCommand extends GenericUserCommand<ResponseUserContext, ResponseUserContextBuilder> {
    public ResponseUserCommand(String name) {
        super(name);
    }
}
