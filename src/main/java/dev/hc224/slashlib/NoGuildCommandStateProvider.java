package dev.hc224.slashlib;

import discord4j.common.util.Snowflake;

import java.util.Set;

/**
 * A default "implementation" for guild command state which throws an exception if ever called.
 * A user-defined implementation should be provided to {@link GenericSlashLibBuilder}
 */
public class NoGuildCommandStateProvider implements GuildCommandStateProvider {
    @Override
    public boolean guildHasChatCommand(Snowflake guildId, String commandName) {
        throw new RuntimeException("GuildCommandStateProvider is not implemented!");
    }

    @Override
    public boolean guildHasUserCommand(Snowflake guildId, String commandName) {
        throw new RuntimeException("GuildCommandStateProvider is not implemented!");
    }

    @Override
    public boolean guildHasMessageCommand(Snowflake guildId, String commandName) {
        throw new RuntimeException("GuildCommandStateProvider is not implemented!");
    }

    @Override
    public Set<String> getGuildChatCommands(Snowflake guildId) {
        throw new RuntimeException("GuildCommandStateProvider is not implemented!");
    }

    @Override
    public Set<String> getGuildUserCommands(Snowflake guildId) {
        throw new RuntimeException("GuildCommandStateProvider is not implemented!");
    }

    @Override
    public Set<String> getGuildMessageCommands(Snowflake guildId) {
        throw new RuntimeException("GuildCommandStateProvider is not implemented!");
    }
}
