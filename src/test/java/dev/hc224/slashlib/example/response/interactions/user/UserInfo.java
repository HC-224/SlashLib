package dev.hc224.slashlib.example.response.interactions.user;

import dev.hc224.slashlib.example.response.CommandResponse;
import dev.hc224.slashlib.example.response.CommandState;
import dev.hc224.slashlib.example.response.slashlib.commands.ResponseUserCommand;
import dev.hc224.slashlib.example.response.slashlib.context.ResponseUserContext;
import dev.hc224.slashlib.example.response.slashlib.context.ResponseUserContextBuilder;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

/**
 * An example command which utilizes the {@link CommandResponse}
 *  instance in the custom {@link ResponseUserContext} provided to respond to the user.
 */
public class UserInfo extends ResponseUserCommand {
    public UserInfo() {
        super("Info");
    }

    @Override
    public Mono<ResponseUserContext> executeUser(ResponseUserContext context) {
        // A bit of a "fake" reactive method, but it is short and doesn't block.
        // We've required the target user be present
        //noinspection OptionalGetWithoutIsPresent,ConstantConditions
        context.getCommandResponse()
            .addEmbed(EmbedCreateSpec.builder()
                .author(context.getTargetUser().get().getUsername(), null, context.getTargetUser().get().getAvatarUrl())
                .addField("Silent Mention", context.getTargetUser().get().getMention(), false))
            .setIsEphemeral(true)
            .setState(CommandState.SUCCESS);
        return Mono.just(context);
    }

    @Override
    public ResponseUserContextBuilder setRequestData(ResponseUserContextBuilder builder) {
        return (ResponseUserContextBuilder) builder.requireTargetUser();
    }
}
