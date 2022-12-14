package dev.hc224.slashlib.example.basic.interactions.chat.info;

import dev.hc224.slashlib.commands.standard.TopGroupCommand;
import dev.hc224.slashlib.example.basic.interactions.chat.info.entity.InfoEntityGroup;

/**
 * A top level group which will have some commands for viewing information about a guild, channel, or role.
 */
public class InfoGroup extends TopGroupCommand {
    /**
     * Create a new instance of this class, the constructor is where details about the interaction are set.
     */
    public InfoGroup() {
        // Pass the name and description to the superclass constructor
        super("info", "information about guild entities");
        // Add the `/info guild` sub command
        addSubCommand(new InfoGuild());
        // Add the `/info entity` mid-group
        addSubCommand(new InfoEntityGroup());
    }
}
