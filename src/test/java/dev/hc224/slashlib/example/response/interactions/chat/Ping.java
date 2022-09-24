package dev.hc224.slashlib.example.response.interactions.chat;

import dev.hc224.slashlib.example.response.CommandResponse;
import dev.hc224.slashlib.example.response.CommandState;
import dev.hc224.slashlib.example.response.slashlib.commands.ResponseTopCommand;
import dev.hc224.slashlib.example.response.slashlib.context.ResponseChatContext;
import reactor.core.publisher.Mono;

/**
 * An example command which utilizes the {@link CommandResponse}
 *  instance in the custom {@link ResponseChatContext} provided to respond to the user.
 */
public class Ping extends ResponseTopCommand {
    // Don't forget to change the visibility to public for commands!
    public Ping() {
        super("ping", "get a pong");
    }

    @Override
    public Mono<ResponseChatContext> executeChat(ResponseChatContext context) {
        // A bit of a "fake" reactive method, but it is short and doesn't block.
        context.getCommandResponse()
            .setContent("Pong!")
            .setState(CommandState.SUCCESS);
        return Mono.just(context);
    }
}
