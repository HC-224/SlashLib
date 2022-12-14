package dev.hc224.slashlib;

import discord4j.common.util.Snowflake;

import java.util.Set;

/**
 * When validating guild commands the state is retrieved for each guild to validate
 *  if the guild should or shouldn't be registered on the guild. Since this is a very
 *  user-dependent implementation on how this data is stored it is upto the user on
 *  how to store and provide this data.
 */
public interface GuildCommandStateProvider {
    /**
     * Get if a guild should have the provided CHAT_INPUT command.
     *
     * @param guildId the {@link Snowflake} ID of the Guild
     * @param commandName the name of the guild command
     * @return true if the Guild is supposed to have the provided command
     */
    boolean guildHasChatCommand(Snowflake guildId, String commandName);

    /**
     * Get if a guild should have the provided USER command.
     *
     * @param guildId the {@link Snowflake} ID of the Guild
     * @param commandName the name of the guild command
     * @return true if the Guild is supposed to have the provided command
     */
    boolean guildHasUserCommand(Snowflake guildId, String commandName);

    /**
     * Get if a guild should have the provided MESSAGE command.
     *
     * @param guildId the {@link Snowflake} ID of the Guild
     * @param commandName the name of the guild command
     * @return true if the Guild is supposed to have the provided command
     */
    boolean guildHasMessageCommand(Snowflake guildId, String commandName);

    /**
     * Get a Set of all the CHAT_INPUT commands a Guild should have by their name.
     *
     * @param guildId the {@link Snowflake} ID of the Guild
     * @return true if the Guild is supposed to have the provided command
     */
    Set<String> getGuildChatCommands(Snowflake guildId);

    /**
     * Get a Set of all the USER commands a Guild should have by their name.
     *
     * @param guildId the {@link Snowflake} ID of the Guild
     * @return true if the Guild is supposed to have the provided command
     */
    Set<String> getGuildUserCommands(Snowflake guildId);

    /**
     * Get a Set of all the MESSAGE commands a Guild should have by their name.
     *
     * @param guildId the {@link Snowflake} ID of the Guild
     * @return true if the Guild is supposed to have the provided command
     */
    Set<String> getGuildMessageCommands(Snowflake guildId);
}
