package dev.hc224.slashlib.example.context.interactions.chat;

import dev.hc224.slashlib.example.context.slashlib.commands.CustomTopCommand;
import dev.hc224.slashlib.example.context.slashlib.context.CustomChatContext;
import dev.hc224.slashlib.example.context.slashlib.context.CustomChatContextBuilder;
import reactor.core.publisher.Mono;

/**
 * An example command which works with custom context classes.
 */
public class Ping extends CustomTopCommand {
    // Don't forget to change the visibility to public for commands!
    public Ping() {
        super("ping", "get a pong with your discriminator");
    }

    @Override
    public Mono<CustomChatContext> executeChat(CustomChatContext context) {
        return context.getEvent()
            .reply("Pong! Your Discriminator is `" + context.getUserDiscriminator() + "`")
            .thenReturn(context);
    }

    @Override
    public CustomChatContextBuilder setRequestData(CustomChatContextBuilder builder) {
        // Another exposure of type issues, the return type will need to be cast if using
        //  a superclass method.
        // The member is not actually required for this example, but is included to display
        //  this quirk. But can still be accessed if needed.
        // This also means that all required data from the lowest class should be called first,
        //  then data from the superclass.
        // It is not recommended to add override methods on the custom context class with casts
        //  within them, as that can/will result in multiple casts compared to just once.
        return (CustomChatContextBuilder) builder.requireUserDiscriminator().requireMember();
    }
}
