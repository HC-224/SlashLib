package dev.hc224.slashlib.example.guild.interactions.user;

import dev.hc224.slashlib.commands.standard.UserCommand;
import dev.hc224.slashlib.context.UserContext;
import dev.hc224.slashlib.context.UserContextBuilder;
import reactor.core.publisher.Mono;

/**
 * Basic user context command that returns the display name of the target member.
 * This will be the same as the username unless a nickname is set.
 */
public class MemberInfo extends UserCommand {
    public MemberInfo() {
        super("Info - Member");
    }

    @Override
    public Mono<UserContext> executeUser(UserContext context) {
        // We've required the target member be present
        //noinspection OptionalGetWithoutIsPresent,ConstantConditions
        return context.getEvent()
            .reply("Target Member Display Name is `" + context.getTargetMember().get().getDisplayName() + "`")
            .thenReturn(context);
    }

    @Override
    public UserContextBuilder setRequestData(UserContextBuilder builder) {
        return builder.requireTargetMember();
    }
}
