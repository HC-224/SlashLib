package dev.hc224.slashlib.commands.generic;

import dev.hc224.slashlib.commands.BaseCommand;
import dev.hc224.slashlib.context.MessageContext;
import dev.hc224.slashlib.context.MessageContextBuilder;
import discord4j.core.object.command.ApplicationCommand;
import discord4j.discordjson.json.ApplicationCommandRequest;
import reactor.core.publisher.Mono;

/**
 * An abstract class representing a MESSAGE context menu command.
 *
 * @param <IC> the {@link MessageContext} class provided to commands for execution.
 * @param <IB> the {@link MessageContextBuilder} class provided to commands to set requested data.
 */
public abstract class GenericMessageCommand<IC extends MessageContext, IB extends MessageContextBuilder> extends BaseCommand {
    protected GenericMessageCommand(String name) {
        // Discord docs state that the description should be an empty string
        super(name, "", null, ApplicationCommand.Type.MESSAGE);
    }

    public abstract Mono<IC> executeMessage(IC context);

    /**
     * Set the required data for this interaction to be executed. By default, nothing is required.
     *
     * @param contextBuilder the context builder that will be used to create the interaction context.
     * @return the context builder modified
     */
    public IB setRequestData(IB contextBuilder) {
        return contextBuilder;
    }

    /**
     * @return a representative {@link ApplicationCommandRequest} to compare/create this data with Discord
     */
    @Override
    public ApplicationCommandRequest asRequest() {
        return this.buildBaseRequest().build();
    }
}
