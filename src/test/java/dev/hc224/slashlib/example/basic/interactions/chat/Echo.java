package dev.hc224.slashlib.example.basic.interactions.chat;

import dev.hc224.slashlib.commands.standard.TopCommand;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.utility.OptionBuilder;
import discord4j.core.object.entity.Attachment;
import reactor.core.publisher.Mono;

/**
 * An example chat input interaction which is usable in Direct Messages (DMs).
 * It takes an input and replies to the user with it.
 *
 * As it is usable in DMs, trying to require any data related to a guild will cause it to fail.
 */
public class Echo extends TopCommand {
    public Echo() {
        super("echo", "have a message echoed back to you");
        addOption(OptionBuilder.requiredString("message", "the message to say back to you").build());
        addOption(OptionBuilder.optionalAttachment("file", "a file to repost back to you").build());
    }

    @Override
    public Mono<ChatContext> executeChat(ChatContext context) {
        return Mono.justOrEmpty(context.getOptions().getString("message"))
            .flatMap(message -> context.getEvent().reply(message + ", with `"
                + context.getOptions().getAttachment("file").map(Attachment::getFilename).orElse("nothing")
                + "` attached").withEphemeral(true))
            .thenReturn(context);
    }
}
