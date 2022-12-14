package dev.hc224.slashlib.example.basic.interactions.chat.info.entity;

import dev.hc224.slashlib.commands.standard.MidGroupCommand;

/**
 * A mid level group which will have commands for viewing information about a channel or role.
 * A top level group is different in that it goes on the top level of interactions.
 * This class can be added as a "sub command" to a top group command.
 */
public class InfoEntityGroup extends MidGroupCommand {
    public InfoEntityGroup() {
        super("entity", "information about guild entities");
        addSubCommand(new InfoChannel());
        addSubCommand(new InfoRole());
    }
}
