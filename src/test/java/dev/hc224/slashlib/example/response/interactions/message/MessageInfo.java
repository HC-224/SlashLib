package dev.hc224.slashlib.example.response.interactions.message;

import dev.hc224.slashlib.example.response.CommandResponse;
import dev.hc224.slashlib.example.response.CommandState;
import dev.hc224.slashlib.example.response.slashlib.commands.ResponseMessageCommand;
import dev.hc224.slashlib.example.response.slashlib.context.ResponseMessageContext;
import dev.hc224.slashlib.example.response.slashlib.context.ResponseMessageContextBuilder;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

/**
 * An example command which utilizes the {@link CommandResponse}
 *  instance in the custom {@link ResponseMessageContext} provided to respond to the user.
 */
public class MessageInfo extends ResponseMessageCommand {
    public MessageInfo() {
        super("Info");
    }

    @Override
    public Mono<ResponseMessageContext> executeMessage(ResponseMessageContext context) {
        // A bit of a "fake" reactive method, but it is short and doesn't block.
        // We've required the message author be present
        //noinspection OptionalGetWithoutIsPresent
        context.getCommandResponse()
            .addEmbed(EmbedCreateSpec.builder()
                .author(context.getMessageAuthor().get().getUsername(), null, context.getMessageAuthor().get().getAvatarUrl())
                .description(context.getTargetMessage().get().getContent()))
            .setState(CommandState.SUCCESS);
        return Mono.just(context);
    }

    @Override
    public ResponseMessageContextBuilder setRequestData(ResponseMessageContextBuilder builder) {
        return (ResponseMessageContextBuilder) builder.requireMessage().requireMessageAuthor();
    }
}
