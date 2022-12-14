package dev.hc224.slashlib.example.guild;

import dev.hc224.slashlib.GuildCommandStateProvider;
import discord4j.common.util.Snowflake;

import java.util.*;

/**
 * A very basic and *non-persistent* store of guild command states.
 *
 * States are defined in source code and do not change, as the changes will not persist across a restart.
 *
 * A massive consideration with this system (and whichever one you decide to implement) is that there is no
 *  direct link between the command names and the strings provided here.
 */
public class NonPersistentGuildCommandStateProvider implements GuildCommandStateProvider {
    private final Map<Snowflake, Set<String>> guildChatCommands;
    private final Map<Snowflake, Set<String>> guildUserCommands;
    private final Map<Snowflake, Set<String>> guildMessageCommands;

    private static NonPersistentGuildCommandStateProvider singleton = null;

    public static NonPersistentGuildCommandStateProvider getInstance() {
        if (singleton == null) { singleton = new NonPersistentGuildCommandStateProvider(); }
        return singleton;
    }

    private NonPersistentGuildCommandStateProvider() {
        this.guildChatCommands = new HashMap<>();
        this.guildUserCommands = new HashMap<>();
        this.guildMessageCommands = new HashMap<>();

        // Set this to the guild you use for testing.
        Snowflake testGuildId = Snowflake.of(779410814528389140L);
        // Chat
        HashSet<String> testGuildChatCommands = new HashSet<>();
        testGuildChatCommands.add("ping");
        testGuildChatCommands.add("sync-guild-commands");
        this.guildChatCommands.put(testGuildId, testGuildChatCommands);
        // User
        HashSet<String> testGuildUserCommands = new HashSet<>();
        testGuildUserCommands.add("Info - Member");
        this.guildUserCommands.put(testGuildId, testGuildUserCommands);
        // Message
        HashSet<String> testGuildMessageCommands = new HashSet<>();
        testGuildMessageCommands.add("Info - Reactions");
        this.guildMessageCommands.put(testGuildId, testGuildMessageCommands);
    }

    @Override
    public boolean guildHasChatCommand(Snowflake guildId, String commandName) {
        return Optional.ofNullable(guildChatCommands.get(guildId))
            .map(set -> set.contains(commandName))
            .orElse(false);
    }

    @Override
    public boolean guildHasUserCommand(Snowflake guildId, String commandName) {
        return Optional.ofNullable(guildUserCommands.get(guildId))
            .map(set -> set.contains(commandName))
            .orElse(false);
    }

    @Override
    public boolean guildHasMessageCommand(Snowflake guildId, String commandName) {
        return Optional.ofNullable(guildMessageCommands.get(guildId))
            .map(set -> set.contains(commandName))
            .orElse(false);
    }

    @Override
    public Set<String> getGuildChatCommands(Snowflake guildId) {
        return Collections.unmodifiableSet(Optional.ofNullable(guildChatCommands.get(guildId)).orElse(new HashSet<>()));
    }

    @Override
    public Set<String> getGuildUserCommands(Snowflake guildId) {
        return Collections.unmodifiableSet(Optional.ofNullable(guildUserCommands.get(guildId)).orElse(new HashSet<>()));
    }

    @Override
    public Set<String> getGuildMessageCommands(Snowflake guildId) {
        return Collections.unmodifiableSet(Optional.ofNullable(guildMessageCommands.get(guildId)).orElse(new HashSet<>()));
    }
}
