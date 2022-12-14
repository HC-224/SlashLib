package dev.hc224.slashlib.example.basic.interactions.user;

import dev.hc224.slashlib.commands.standard.UserCommand;
import dev.hc224.slashlib.context.UserContext;
import dev.hc224.slashlib.context.UserContextBuilder;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

/**
 * An example message context interaction.
 * This command can be called by going to the context menu of a user under "Apps".
 */
public class UserInfo extends UserCommand {
    public UserInfo() {
        // Message and User context interactions have no description or options
        // Note that this has the same name as MessageInfo
        super("Info");
        // Since we respond with an embed, we need the EMBED_LINKS permission
        // The default handler will respond with a message about the missing permissions
        //  and not execute this interaction if missing.
        setBotPermissions(Permission.EMBED_LINKS);
    }

    @Override
    public Mono<UserContext> executeUser(UserContext context) {
        // We can safely call Optional#get() since we've required the data be present
        //noinspection ConstantConditions
        return context.getEvent().reply(InteractionApplicationCommandCallbackSpec.builder()
            .addEmbed(EmbedCreateSpec.builder()
                .author(context.getTargetUser().get().getUsername(), null, context.getTargetUser().get().getAvatarUrl())
                .description(context.getTargetUser().get().getMention())
                .build())
            .build())
            .thenReturn(context);
    }

    @Override
    public UserContextBuilder setRequestData(UserContextBuilder contextBuilder) {
        // Require the user this command was called on
        return contextBuilder.requireTargetUser();
    }
}
