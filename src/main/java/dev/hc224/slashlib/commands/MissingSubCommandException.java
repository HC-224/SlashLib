package dev.hc224.slashlib.commands;

/**
 * An exception thrown when no subcommands for a GenericGroupCommand exists when synchronizing commands with Discord.
 *
 * The app should not ignore or continue after this exception is thrown. The command structure should be fixed instead.
 */
public class MissingSubCommandException extends RuntimeException {
    public MissingSubCommandException(BaseCommand command) {
        super("Command group " + command.getClass().getSimpleName() + " has no subcommands!");
    }
}
