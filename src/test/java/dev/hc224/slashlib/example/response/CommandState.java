package dev.hc224.slashlib.example.response;

/**
 * The states which a command can reach during execution.
 */
public enum CommandState {
    SUCCESS         (true, false),  // Command execution successful, send a response.
    SUCCESS_SILENT  (false, false), // Command execution successful, it sent the response already.
    FAILED          (true, false),  // Command execution failed, send a response same as SUCCESS.
    FAILED_SILENT   (false, false), // Command execution failed, it sent the response already.
    ERROR           (true, true),   // There was an error during execution that wasn't handled.
    UNKNOWN         (true, true);   // The state wasn't set, send back a generic response about it.

    private final boolean respond;
    private final boolean error;

    CommandState(boolean respond, boolean error) {
        this.respond = respond;
        this.error = error;
    }

    /**
     * @return true if the {@link CommandResponse} should build a message response and reply to the user
     */
    public boolean shouldRespond() { return respond; }

    /**
     * @return true if the command encountered an error and the {@link CommandResponse}
     *         should generate a generic error message in response.
     */
    public boolean isError() { return error; }
}
