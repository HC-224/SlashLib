package dev.hc224.slashlib.example.response.interactions.chat;

import dev.hc224.slashlib.example.response.CommandState;
import dev.hc224.slashlib.example.response.slashlib.commands.ResponseTopCommand;
import dev.hc224.slashlib.example.response.slashlib.context.ResponseChatContext;
import reactor.core.publisher.Mono;

/**
 * An example command which doesn't set its state so the event receiver will respond.
 */
public class UnknownState extends ResponseTopCommand {
    public UnknownState() {
        super("unknown-state", "test unknown state handling");
    }

    @Override
    public Mono<ResponseChatContext> executeChat(ResponseChatContext context) {
        // Should only occur when the state isn't set, either from forgetting or not considering
        //  all logical paths.
        //noinspection ConstantConditions
        if (1 == 2) {
            context.getCommandResponse()
                .setContent("1 is equal to 2?")
                .setState(CommandState.SUCCESS);
        }
        return Mono.just(context);
    }
}
