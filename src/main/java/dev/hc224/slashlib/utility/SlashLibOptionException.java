package dev.hc224.slashlib.utility;

/**
 * An exception thrown when an option is incorrectly configured with {@link OptionBuilder}
 */
public class SlashLibOptionException extends RuntimeException {
    SlashLibOptionException(OptionBuilder optionBuilder, String message) {
        super("Error while configuring OptionBuilder! " + optionBuilder.type.toString() + " Command, Name:"
                + optionBuilder.name + ", Description:" + optionBuilder.description + ", Reason: " + message);
    }
}
