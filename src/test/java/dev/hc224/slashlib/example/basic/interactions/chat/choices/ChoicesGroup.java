package dev.hc224.slashlib.example.basic.interactions.chat.choices;

import dev.hc224.slashlib.commands.standard.TopGroupCommand;

public class ChoicesGroup extends TopGroupCommand {
    public ChoicesGroup() {
        super("choices", "commands which offer choices and autocomplete for options");
        addSubCommand(new ChoicesColors());
        addSubCommand(new ChoicesAnimals());
    }
}
