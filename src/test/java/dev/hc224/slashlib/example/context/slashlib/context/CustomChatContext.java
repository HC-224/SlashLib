package dev.hc224.slashlib.example.context.slashlib.context;

import dev.hc224.slashlib.context.ChatContext;

/**
 * An example chat input context with custom fields to be provided to commands.
 */
public class CustomChatContext extends ChatContext {
    protected final String userDiscriminator;
    protected final Boolean valueThatNeverExists;

    public CustomChatContext(CustomChatContextBuilder builder) {
        super(builder);
        // It is recommended to put the builder and this class in the same package to take advantage
        //  of package private accessibility.
        this.userDiscriminator = builder.userDiscriminator;
        this.valueThatNeverExists = builder.valueThatNeverExists;
    }

    // Currently, since the values are explicitly required and execution does not happen
    //  unless all required values are present, there is no need to wrap these in optional.
    public String getUserDiscriminator()     { return userDiscriminator; }
    public Boolean getValueThatNeverExists() { return valueThatNeverExists; }
}
