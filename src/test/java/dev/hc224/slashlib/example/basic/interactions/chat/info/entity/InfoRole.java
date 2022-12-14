package dev.hc224.slashlib.example.basic.interactions.chat.info.entity;

import dev.hc224.slashlib.commands.standard.SubCommand;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.utility.OptionBuilder;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

/**
 * An example chat input interaction which belongs to a top or mid level group.
 * This command can be called with `/info entity role`
 */
class InfoRole extends SubCommand {
    /**
     * Create a new instance of this class, we will need a role specified as an option.
     */
    InfoRole() {
        super("role", "show information about a role");
        // We will require a role here unlike in the channel example
        // We require no data since we don't need the channel this interaction
        //  was called in under the event the option isn't provided.
        addOption(OptionBuilder.requiredRole("role", "the role to show info about").build());
        // Since we respond with an embed, we need the EMBED_LINKS permission
        // The default handler will respond with a message about the missing permissions
        //  and not execute this interaction if missing.
        setBotPermissions(Permission.EMBED_LINKS);
    }

    /**
     * @param context a {@link ChatContext} provided by SlashLib with some data provided about the interaction.
     * @return the same context provided
     */
    @Override
    public Mono<ChatContext> executeChat(ChatContext context) {
        return context.getOptions().getRole("role")
            .map(role -> InteractionApplicationCommandCallbackSpec.builder()
                .addEmbed(EmbedCreateSpec.builder()
                    .title(role.getName())
                    .description("Mention: " + role.getMention())
                    .build())
                .build())
            .flatMap(spec -> context.getEvent().reply(spec))
            .thenReturn(context);
    }
}
