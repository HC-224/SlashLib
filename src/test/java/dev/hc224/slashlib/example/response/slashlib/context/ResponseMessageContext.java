package dev.hc224.slashlib.example.response.slashlib.context;

import dev.hc224.slashlib.context.MessageContext;
import dev.hc224.slashlib.context.MessageContextBuilder;
import dev.hc224.slashlib.example.response.CommandResponse;

/**
 * A Custom MESSAGE context class which provides a {@link CommandResponse} to the command
 *  lifecycle, which is used in the event receiver to respond to the event.
 *
 * Note that this class does not need to have its own builder, as the additional data is always provided.
 */
public class ResponseMessageContext extends MessageContext {
    private final CommandResponse commandResponse;

    public ResponseMessageContext(MessageContextBuilder builder) {
        super(builder);
        // This is created here vs in the builder as the builder has no need for this
        this.commandResponse = new CommandResponse();
    }

    /** @return get the {@link CommandResponse} used to contain the response to the interaction */
    public CommandResponse getCommandResponse() { return commandResponse; }
}
