package dev.hc224.slashlib.example.basic.interactions.chat.info.entity;

import dev.hc224.slashlib.commands.standard.SubCommand;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;
import dev.hc224.slashlib.utility.OptionBuilder;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

/**
 * An example chat input interaction which belongs to a top or mid level group.
 * This command can be called with `/info entity channel`
 */
class InfoChannel extends SubCommand {
    /**
     * Create a new instance of this class, we will need a channel specified as an option.
     */
    InfoChannel() {
        super("channel", "show information about a channel");
        // We add options the interaction needs here
        // We can use the OptionBuilder helper class,
        //  but creating the options with raw Discord4J objects will work too
        addOption(OptionBuilder.optionalChannel("channel", "the channel to show info about").build());
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
        // We will get the specified channel from the OptionsList
        // If it is not present we will default to the channel this
        //  interaction was called in
        //noinspection OptionalGetWithoutIsPresent
        return context.getOptions().getChannel("channel")
            // We want any kind of guild channel, which should always be true as
            //  the top level interaction this one belongs to (/info) is not set
            //  to be used in DMs.
            .ofType(GuildChannel.class)
            // We can safely (implicitly) cast a TopLevelGuildChannel to its subclass
            // We can also ignore a safety check on the Optional#get() as we required this data
            .defaultIfEmpty(context.getTopLevelGuildChannel().get())
            .map(channel -> InteractionApplicationCommandCallbackSpec.builder()
                .addEmbed(EmbedCreateSpec.builder()
                    .title(channel.getName())
                    .description("Mention: " + channel.getMention())
                    .build())
                .build())
            .flatMap(spec -> context.getEvent().reply(spec))
            .thenReturn(context);
    }

    /**
     * @param contextBuilder the context builder that will be used to create the interaction context.
     * @return the context builder modified
     */
    @Override
    public ChatContextBuilder setRequestData(ChatContextBuilder contextBuilder) {
        return (ChatContextBuilder) contextBuilder.requireTopLevelGuildChannel();
    }
}
