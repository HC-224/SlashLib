package dev.hc224.slashlib.commands.standard;

import dev.hc224.slashlib.commands.generic.GenericUserCommand;
import dev.hc224.slashlib.context.UserContext;
import dev.hc224.slashlib.context.UserContextBuilder;

/**
 * A wrapper class for {@link GenericUserCommand} to simplify the default/standard usages of SlashLib.
 */
public abstract class UserCommand extends GenericUserCommand<UserContext, UserContextBuilder> {
    protected UserCommand(String name) {
        super(name);
    }
}
