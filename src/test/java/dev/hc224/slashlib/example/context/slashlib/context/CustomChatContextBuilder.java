package dev.hc224.slashlib.example.context.slashlib.context;

import dev.hc224.slashlib.context.ChatContextBuilder;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * An example chat input context builder class which provides extra data not in the core library.
 */
public class CustomChatContextBuilder extends ChatContextBuilder {
    protected String userDiscriminator;
    protected Boolean valueThatNeverExists;

    /**
     * Simply construct the context builder class then set default values for this instance.
     */
    public CustomChatContextBuilder(ChatInputInteractionEvent event, ApplicationCommandInteraction aci, List<ApplicationCommandInteractionOption> options) {
        super(event, aci, options);
        userDiscriminator = null;
        valueThatNeverExists = null;
    }

    /**
     * While not an abstract class (anymore) there must be an override here to
     *  construct an instance of the class. If you don't then your commands will silently be
     *  dropped!
     *
     * @return a new {@link CustomChatContext} created based on data collected from this class
     */
    @Override
    public CustomChatContext build() {
        return new CustomChatContext(this);
    }

    /**
     * An extra requirement that can be specified in client code.
     * This just gets the user discriminator as an explicit field,
     *  but it could be used to get SQL stored data on a user/channel/guild/etc.
     * Useful when a command requires certain data to be executed.
     *
     * @return this instance modified
     */
    public CustomChatContextBuilder requireUserDiscriminator() {
        // Required data will be obtained before executing the command, but not now.
        // We add the mono which will get the data and set the fields of this class.
        getRequiredMonoList().add(
            // Create a mono that just holds the discriminator
            // This would instead be some other way of getting your data
            Mono.just(this.getEvent().getInteraction().getUser().getDiscriminator())
            // If the data is present then set it, if it is not the mono will be empty
            //  and SlashLib will not execute the command since the data isn't present
            .doOnNext(userDiscriminator -> this.userDiscriminator = userDiscriminator)
            // For type compatibility and to show that we successfully got the data,
            //  we will return a dummy int value. The value itself doesn't matter.
            .map(guild -> 1));
        return this;
    }

    /**
     * An extra requirement that can never be satisfied.
     * If this is required by the command then SlashLib will not execute the command.
     *
     * @return this instance modified
     */
    public CustomChatContextBuilder requireNonExistingData() {
        getRequiredMonoList().add(Mono.just(false)
            .filter(Boolean::booleanValue)
            // As we always filter the value this will never be reached cause the mono
            //  will always be empty.
            // A practical example for this is getting data from SQL that doesn't exist.
            .doOnNext(bool -> this.valueThatNeverExists = bool)
            .map(bool -> 1));
        return this;
    }
}
