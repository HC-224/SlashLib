package dev.hc224.slashlib.commands.generic;

import dev.hc224.slashlib.commands.BaseCommand;
import dev.hc224.slashlib.context.UserContext;
import dev.hc224.slashlib.context.UserContextBuilder;
import discord4j.core.object.command.ApplicationCommand;
import discord4j.discordjson.json.ApplicationCommandRequest;
import reactor.core.publisher.Mono;

/**
 * An abstract class representing a User context menu command.
 *
 * @param <IC> the {@link UserContext} class provided to commands for execution.
 * @param <IB> the {@link UserContextBuilder} class provided to commands to set requested data.
 */
public abstract class GenericUserCommand<IC extends UserContext, IB extends UserContextBuilder> extends BaseCommand {
    protected GenericUserCommand(String name) {
        // Discord docs state that the description should be an empty string
        super(name, "", null, ApplicationCommand.Type.USER);
    }

    public abstract Mono<IC> executeUser(IC context);

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
