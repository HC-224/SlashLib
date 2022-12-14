package dev.hc224.slashlib.example.response.interactions.chat;

import dev.hc224.slashlib.example.response.slashlib.commands.ResponseTopCommand;
import dev.hc224.slashlib.example.response.slashlib.context.ResponseChatContext;
import reactor.core.publisher.Mono;

/**
 * An example command which throws an exception when called.
 */
public class ErrorReactor extends ResponseTopCommand {
    public ErrorReactor() {
        super("error-reactor", "emit an error through a mono when called");
    }

    @Override
    public Mono<ResponseChatContext> executeChat(ResponseChatContext context) {
        return Mono.error(new RuntimeException("Forcibly Thrown Exception"));
    }
}
