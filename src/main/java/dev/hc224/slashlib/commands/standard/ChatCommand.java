package dev.hc224.slashlib.commands.standard;

import dev.hc224.slashlib.commands.generic.GenericChatCommand;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;
import discord4j.core.object.command.ApplicationCommandOption;
import reactor.util.annotation.Nullable;

/**
 * A wrapper class for {@link GenericChatCommand} to simplify the default/standard usages of SlashLib.
 */
public abstract class ChatCommand extends GenericChatCommand<ChatContext, ChatContextBuilder> {
    protected ChatCommand(String name, String description, @Nullable ApplicationCommandOption.Type type) {
        super(name, description, type);
    }
}
