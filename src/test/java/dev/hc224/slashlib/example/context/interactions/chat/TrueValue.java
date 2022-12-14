package dev.hc224.slashlib.example.context.interactions.chat;

import dev.hc224.slashlib.example.context.slashlib.commands.CustomTopCommand;
import dev.hc224.slashlib.example.context.slashlib.context.CustomChatContext;
import dev.hc224.slashlib.example.context.slashlib.context.CustomChatContextBuilder;
import reactor.core.publisher.Mono;

/**
 * A Command that requires the custom context data that will never exist.
 */
public class TrueValue extends CustomTopCommand {
    public TrueValue() {
        super("true-value", "run this command when the value in the source code is true");
    }

    @Override
    public Mono<CustomChatContext> executeChat(CustomChatContext context) {
        return context.getEvent()
            .reply("The command ran? The value is `" + context.getValueThatNeverExists() + "`")
            .thenReturn(context);
    }

    @Override
    public CustomChatContextBuilder setRequestData(CustomChatContextBuilder builder) {
        // Require data that never exists, which means the command will never execute.
        return builder.requireNonExistingData();
    }
}
