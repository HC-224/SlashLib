package dev.hc224.slashlib.utility;

import dev.hc224.slashlib.GenericEventReceiverImpl;
import dev.hc224.slashlib.commands.generic.GenericChatCommand;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;
import discord4j.core.object.command.ApplicationCommandInteractionOption;

import java.util.List;

/**
 * An internal class used when searching through multiple levels of commands within
 *  {@link GenericEventReceiverImpl}
 */
public class CommandOptionPair<CI extends ChatContext, CB extends ChatContextBuilder> {
    private final GenericChatCommand<CI, CB> command;
    private final List<ApplicationCommandInteractionOption> optionList;

    public CommandOptionPair(GenericChatCommand<CI, CB> command, List<ApplicationCommandInteractionOption> optionList) {
        this.command = command;
        this.optionList = optionList;
    }

    public GenericChatCommand<CI, CB> getKey() { return command; }
    public List<ApplicationCommandInteractionOption> getValue() { return optionList; }
}
