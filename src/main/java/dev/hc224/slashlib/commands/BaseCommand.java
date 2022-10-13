package dev.hc224.slashlib.commands;

import discord4j.core.object.command.ApplicationCommand;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import reactor.util.annotation.Nullable;

import java.util.Optional;

/**
 * A class which represents all types of Slash Commands. This class should not be directly extended.
 */
public abstract class BaseCommand {
    // Command Name
    private final String name;
    // Command Description
    private final String description;
    // never null: The type of command this is (chat, user, message)
    private final ApplicationCommand.Type commandType;
    // The default permissions needed to use the command
    // null indicates "unset" and will leave this value exempt from requests and data.
    @Nullable
    private PermissionSet defaultMemberPermissions;
    // If we are strictly requiring the member have permissions.
    // Discord allows server admins to change who can use commands from the defaults.
    private boolean enforceMemberPermissions;
    // Permissions needed by the bot
    private PermissionSet botPermissions;
    // If the command can be used in DMs
    private boolean usableInDMs;

    protected BaseCommand(String name,
                          String description,
                          @Nullable ApplicationCommandOption.Type type,
                          ApplicationCommand.Type commandType) {
        this.name = name;
        this.description = description;
        this.commandType = commandType;

        this.defaultMemberPermissions = null;
        this.enforceMemberPermissions = false;
        this.botPermissions = PermissionSet.none();
        this.usableInDMs = false;
    }

    public abstract ApplicationCommandRequest asRequest();

    /**
     * @return a starting {@link ImmutableApplicationCommandRequest.Builder} with common properties
     */
    protected ImmutableApplicationCommandRequest.Builder buildBaseRequest() {
        ImmutableApplicationCommandRequest.Builder builder = ApplicationCommandRequest.builder()
                .type(this.getCommandType().getValue())
                .name(this.getName())
                .description(this.getDescription());
        if (this.defaultMemberPermissions != null) { // Only provide default permissions if they are set
            builder.defaultMemberPermissions(String.valueOf(this.defaultMemberPermissions.getRawValue()));
        }
        return builder;
    }

    /**
     * Set the permissions needed for members to use this command.
     * Discord evaluates this and allows/denies roles/members automatically based on this.
     * Note that server moderators can override this and this does not guarantee all users that use
     *  this command will have the permissions.
     * <p>
     * Leave this unset for @everyone to be able to use the command.
     *
     * @param permissions a list of permissions that a member needs to use the command by default
     */
    protected void setDefaultMemberPermissions(Permission ... permissions) {
        this.defaultMemberPermissions = PermissionSet.of(permissions);
    }

    /**
     * Use classic behavior and enforce that calling members must have the default permissions.
     * This can be useful for ensuring that a misconfigured command won't be misused.
     * <p>
     * Discord behavior is to enforce permissions unless a server admin overrides it.
     * This will enforce that calling users have the permissions regardless of what server owners change.
     * As such, this should be used sparingly to not remove too much control from server admins.
     * <p>
     * This should be called after {@link BaseCommand#setDefaultMemberPermissions(Permission...)}
     *
     * @throws IllegalStateException when no default member permissions have been set
     */
    protected void setEnforceMemberPermission() {
        if (this.defaultMemberPermissions == null) {
            throw new IllegalStateException("No member permissions have been set!");
        }
        this.enforceMemberPermissions = true;
    }

    /**
     * Set that this command can be used in DMs.
     */
    protected void setUsableInDMs() {
        this.usableInDMs = true;
    }

    /**
     * Set the permissions the bot needs to execute this command.
     *
     * @param permissions a unique list of Discord permissions
     */
    protected void setBotPermissions(Permission... permissions) {
        this.botPermissions = PermissionSet.of(permissions);
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public ApplicationCommand.Type getCommandType() { return commandType; }
    public Optional<PermissionSet> getDefaultMemberPermissions() { return Optional.ofNullable(defaultMemberPermissions); }
    public boolean getEnforceMemberPermissions() { return enforceMemberPermissions; }
    public PermissionSet getBotPermissions() { return botPermissions; }
    public boolean isUsableInDMs() { return usableInDMs; }
}
