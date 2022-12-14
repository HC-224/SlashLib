package dev.hc224.slashlib.example.basic.interactions.message;

import dev.hc224.slashlib.commands.standard.MessageCommand;
import dev.hc224.slashlib.context.MessageContext;
import dev.hc224.slashlib.context.MessageContextBuilder;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

/**
 * An example message context interaction.
 * This command can be called by going to the context menu of a message under "Apps".
 */
public class MessageInfo extends MessageCommand {
    public MessageInfo() {
        // Message and User context interactions have no description or options
        // Note that this has the same name as UserInfo
        super("Info");
        // Since we respond with an embed, we need the EMBED_LINKS permission
        // The default handler will respond with a message about the missing permissions
        //  and not execute this interaction if missing.
        setBotPermissions(Permission.EMBED_LINKS);
    }

    @Override
    public Mono<MessageContext> executeMessage(MessageContext context) {
        // We can safely call Optional#get() since we've required the data be present
        //noinspection OptionalGetWithoutIsPresent
        return context.getEvent().reply(InteractionApplicationCommandCallbackSpec.builder()
            .addEmbed(EmbedCreateSpec.builder()
                .author(context.getMessageAuthor().get().getUsername(), null, context.getMessageAuthor().get().getAvatarUrl())
                .description((context.getTargetMessage().get().getContent().isEmpty())
                    ? context.getTargetMessage().get().getAttachments().size() + " attachments"
                    : context.getTargetMessage().get().getContent())
                .build())
            .build())
            .thenReturn(context);
    }

    @Override
    public MessageContextBuilder setRequestData(MessageContextBuilder contextBuilder) {
        // Require the message this command was called on.
        return contextBuilder.requireMessage().requireMessageAuthor();
    }
}
