package dev.hc224.slashlib.example.guild.interactions.user;

import dev.hc224.slashlib.commands.standard.UserCommand;
import dev.hc224.slashlib.context.UserContext;
import dev.hc224.slashlib.context.UserContextBuilder;
import reactor.core.publisher.Mono;

/**
 * Basic user context command that returns the username of the target user.
 */
public class UserInfo extends UserCommand {
    public UserInfo() {
        super("Info - User");
        setUsableInDMs();
    }

    @Override
    public Mono<UserContext> executeUser(UserContext context) {
        // We've required the target user be present
        //noinspection OptionalGetWithoutIsPresent,ConstantConditions
        return context.getEvent()
            .reply("Target Username is `" + context.getTargetUser().get().getUsername() + "`")
            .thenReturn(context);
    }

    @Override
    public UserContextBuilder setRequestData(UserContextBuilder builder) {
        return builder.requireTargetUser();
    }
}
