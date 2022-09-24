package dev.hc224.slashlib.example.response.slashlib.context;

import dev.hc224.slashlib.context.UserContext;
import dev.hc224.slashlib.context.UserContextBuilder;
import dev.hc224.slashlib.example.response.CommandResponse;

/**
 * A Custom USER context class which provides a {@link CommandResponse} to the command
 *  lifecycle, which is used in the event receiver to respond to the event.
 *
 * Note that this class does not need to have its own builder, as the additional data is always provided.
 */
public class ResponseUserContext extends UserContext {
    private final CommandResponse commandResponse;

    public ResponseUserContext(UserContextBuilder builder) {
        super(builder);
        // This is created here vs in the builder as the builder has no need for this
        this.commandResponse = new CommandResponse();
    }

    /** @return get the {@link CommandResponse} used to contain the response to the interaction */
    public CommandResponse getCommandResponse() { return commandResponse; }
}
