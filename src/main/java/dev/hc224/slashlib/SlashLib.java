package dev.hc224.slashlib;

import dev.hc224.slashlib.context.*;

/**
 * A simple wrapper around {@link GenericSlashLib} to aid in ease of use for simple
 *  setup and configuration.
 *
 * However, some underlying components will show.
 */
public class SlashLib
        extends GenericSlashLib<
        ChatContext, ChatContextBuilder,
        UserContext, UserContextBuilder,
        MessageContext, MessageContextBuilder
        >{

    protected SlashLib(SlashLibBuilder builder) {
        super(builder);
    }

    static SlashLib create(SlashLibBuilder builder) {
        if (created) {
            throw new IllegalStateException("GenericSlashLib already created!");
        }
        SlashLib slashLib = new SlashLib(builder);
        slashLib.receiver = builder.eventReceiverProducer.apply(slashLib);
        slashLib.commandRegister = CommandRegister.create(
                builder.globalChatCommands,
                builder.globalUserCommands,
                builder.globalMessageCommands,
                builder.guildChatCommands,
                builder.guildUserCommands,
                builder.guildMessageCommands,
                builder.guildCommandStateProvider);
        created = true;
        return slashLib;
    }

    @Override
    public CommandRegister<
            ChatContext, ChatContextBuilder,
            UserContext, UserContextBuilder,
            MessageContext, MessageContextBuilder
            > getCommandRegister() {
        return super.getCommandRegister();
    }
}
