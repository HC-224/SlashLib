package dev.hc224.slashlib.example.response.interactions.chat;

import dev.hc224.slashlib.example.response.slashlib.commands.ResponseTopCommand;
import dev.hc224.slashlib.example.response.slashlib.context.ResponseChatContext;
import reactor.core.publisher.Mono;

/**
 * An example command which throws an exception when called.
 */
public class Error extends ResponseTopCommand {
    public Error() {
        super("error", "throw an uncaught exception during execution");
    }

    @Override
    public Mono<ResponseChatContext> executeChat(ResponseChatContext context) {
        throw new RuntimeException("Forcibly Thrown Exception");
    }
}
