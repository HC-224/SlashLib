package dev.hc224.slashlib.example.basic.interactions.chat.info;

import dev.hc224.slashlib.commands.standard.SubCommand;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.rest.util.Image;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * An example chat input interaction which belongs to a top or mid level group.
 * This command can be called with `/info guild`
 */
class InfoGuild extends SubCommand {
    /**
     * Create a new instance of this class, we have no options to set for it.
     * But do note, due to how the classes are arranged in the packages this
     *  constructor doesn't need to be public, and neither does the class.
     *  This does not affect things greatly, but it won't make this class
     *  available where it isn't needed.
     */
    InfoGuild() {
        super("guild", "show information about this guild");
    }

    /**
     * For this command we will focus on how to use blocking code.
     * To do this we simply create a mono from the context then *map* to a locally created
     *  non-reactive method.
     *
     * @param context a {@link ChatContext} provided by SlashLib with some data provided about the interaction.
     * @return the same context provided
     */
    @Override
    public Mono<ChatContext> executeChat(ChatContext context) {
        return Mono.just(context).map(this::executeChatBlocking);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent") // We can safely call Optional#get() for the guild since we required it
    private ChatContext executeChatBlocking(ChatContext context) {
        EmbedCreateSpec.Builder embed = EmbedCreateSpec.builder();

        embed.title(context.getGuild().get().getName());
        // There "should" always be a PNG version of an avatar available.
        //noinspection OptionalGetWithoutIsPresent
        embed.thumbnail(context.getGuild().get().getIconUrl(Image.Format.PNG).get());

        // Let's use a StringBuilder with some blocking calls since we are in a blocking method.
        // A normal string can work too.
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
            .append("Users: ").append(context.getGuild().get().getMemberCount())
            .append("\n")
            .append("Channels: ").append(context.getGuild().get()
                .getChannels()
                .collectList()
                .map(List::size)
                .defaultIfEmpty(-1)
                .block())
            .append("\n")
            .append("Roles: ").append(context.getGuild().get()
                .getRoles()
                .collectList()
                .map(List::size)
                .defaultIfEmpty(-1)
                .block());
        embed.description(stringBuilder.toString());

        // Reply with our embed
        context.getEvent()
            .reply(InteractionApplicationCommandCallbackSpec.builder()
                .addEmbed(embed.build())
                .build())
            .block();

        return context;
    }

    /**
     * Set the required data needed to execute this interaction. The default event receiver
     *  will handle sending an error message if the required data can't be retrieved.
     *
     * @param contextBuilder the context builder that will be used to create the interaction context.
     * @return the context builder modified
     */
    @Override
    public ChatContextBuilder setRequestData(ChatContextBuilder contextBuilder) {
        return (ChatContextBuilder) contextBuilder.requireGuild();
    }
}
