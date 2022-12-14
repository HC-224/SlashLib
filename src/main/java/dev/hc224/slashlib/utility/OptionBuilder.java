package dev.hc224.slashlib.utility;

import dev.hc224.slashlib.CommandRegister;
import dev.hc224.slashlib.commands.standard.ChatCommand;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.core.object.entity.channel.Channel;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ImmutableApplicationCommandOptionData;

/**
 * A utility class to build {@link ApplicationCommandOptionData} for commands in an appropriate
 *  fashion to be used with the {@link CommandRegister} updating process.
 *
 * The option data is built as this class is built.
 *
 * The most notorious "gotcha" as of D4J 3.2.0 SNAPSHOT (2021-08-27) is that required must be empty
 *  to be considered as false.
 */
@SuppressWarnings("UnusedReturnValue")
public class OptionBuilder {
    final String name;
    final String description;
    final ApplicationCommandOption.Type type;

    final ImmutableApplicationCommandOptionData.Builder option;

    // Keep track of these to quickly reference them when needed.
    private int choiceCount;
    private boolean autocomplete;

    public OptionBuilder(String name, String description, ApplicationCommandOption.Type type) {
        this.name = name;
        this.description = description;
        this.type = type;

        this.option = ApplicationCommandOptionData.builder().name(name).description(description).type(type.getValue());

        this.choiceCount = 0;
        this.autocomplete = false;
    }

    /**
     * Shortcut to create an optional string option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to an optional string
     */
    public static OptionBuilder optionalString(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.STRING);
    }

    /**
     * Shortcut to create a required string option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to a required string
     */
    public static OptionBuilder requiredString(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.STRING).setIsRequired();
    }

    /**
     * Shortcut to create an optional integer option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to an optional integer
     */
    public static OptionBuilder optionalInteger(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.INTEGER);
    }

    /**
     * Shortcut to create a required integer option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to a required integer
     */
    public static OptionBuilder requiredInteger(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.INTEGER).setIsRequired();
    }

    /**
     * Shortcut to create an optional boolean option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to an optional boolean
     */
    public static OptionBuilder optionalBoolean(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.BOOLEAN);
    }

    /**
     * Shortcut to create a required boolean option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to a required boolean
     */
    public static OptionBuilder requiredBoolean(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.BOOLEAN).setIsRequired();
    }

    /**
     * Shortcut to create an optional user option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to an optional user
     */
    public static OptionBuilder optionalUser(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.USER);
    }

    /**
     * Shortcut to create a required user option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to a required user
     */
    public static OptionBuilder requiredUser(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.USER).setIsRequired();
    }

    /**
     * Shortcut to create an optional channel option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to an optional channel
     */
    public static OptionBuilder optionalChannel(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.CHANNEL);
    }

    /**
     * Shortcut to create a required channel option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to a required channel
     */
    public static OptionBuilder requiredChannel(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.CHANNEL).setIsRequired();
    }

    /**
     * Shortcut to create an optional role option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to an optional role
     */
    public static OptionBuilder optionalRole(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.ROLE);
    }

    /**
     * Shortcut to create a required role option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to a required role
     */
    public static OptionBuilder requiredRole(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.ROLE).setIsRequired();
    }

    /**
     * Shortcut to create an optional mentionable option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to an optional mentionable
     */
    public static OptionBuilder optionalMentionable(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.MENTIONABLE);
    }

    /**
     * Shortcut to create a required mentionable option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to a required mentionable
     */
    public static OptionBuilder requiredMentionable(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.MENTIONABLE).setIsRequired();
    }

    /**
     * Shortcut to create an optional number option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to an optional number
     */
    public static OptionBuilder optionalNumber(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.NUMBER);
    }

    /**
     * Shortcut to create a required number option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to a required number
     */
    public static OptionBuilder requiredNumber(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.NUMBER).setIsRequired();
    }

    /**
     * Shortcut to create an optional attachment option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to an optional attachment
     */
    public static OptionBuilder optionalAttachment(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.ATTACHMENT);
    }

    /**
     * Shortcut to create a required attachment option.
     *
     * @param name name of the option
     * @param description description of the option
     * @return a new {@link OptionBuilder} initialized to a required attachment
     */
    public static OptionBuilder requiredAttachment(String name, String description) {
        return new OptionBuilder(name, description, ApplicationCommandOption.Type.ATTACHMENT).setIsRequired();
    }

    /**
     * State that this option must be provided by the user when calling the command.
     * Implicitly called on any `requiredXXX(name, description)` method
     *
     * @return this instance
     */
    public OptionBuilder setIsRequired() {
        this.option.required(true);
        return this;
    }

    /**
     * Add a choice to this option using Discord4Js {@link ApplicationCommandOptionChoiceData}
     * AutoComplete and Choices are mutually exclusive.
     *
     * @param choice the choice data to add
     * @return this instance
     * @throws SlashLibOptionException when attempting to add a choice when autocomplete is enabled
     */
    public OptionBuilder addChoice(ApplicationCommandOptionChoiceData choice) {
        if (!(this.type.equals(ApplicationCommandOption.Type.STRING)
                || this.type.equals(ApplicationCommandOption.Type.INTEGER)
                || this.type.equals(ApplicationCommandOption.Type.NUMBER))) {
            throw new SlashLibOptionException(this, "Only STRING, INTEGER, and NUMBER option types can have choices.");
        }
        if (this.choiceCount >= 25) {
            throw new SlashLibOptionException(this, "Exceeded number of choices an option can have! (25 max)");
        }
        if (this.autocomplete) {
            throw new SlashLibOptionException(this, "Cannot have autocomplete enabled and choices.");
        }
        this.choiceCount++;
        this.option.addChoice(choice);
        return this;
    }

    /**
     * Shortcut method to add a choice to this option using {@link ApplicationCommandOptionChoiceData#builder()}
     * AutoComplete and Choices are mutually exclusive.
     *
     * @param name the display name of the value the user will see
     * @param value the actual value set for the option
     * @return this instance
     * @throws SlashLibOptionException when attempting to add a choice when autocomplete is enabled
     */
    public OptionBuilder addChoice(String name, Object value) {
        this.addChoice(ApplicationCommandOptionChoiceData.builder().name(name).value(value).build());
        return this;
    }

    /**
     * Restrict the types of channels that can be provided to this option if it is a channel type.
     *
     * @param channelTypes the types of channel to allow as a value for this option
     * @return this instance
     * @throws SlashLibOptionException when setting channel types for a non CHANNEL option
     */
    public OptionBuilder setChannelTypes(Channel.Type ... channelTypes) {
        if (!this.type.equals(ApplicationCommandOption.Type.CHANNEL)) {
            throw new SlashLibOptionException(this, "Channel Types can only be set for CHANNEL options.");
        }
        for (Channel.Type type : channelTypes) {
            this.option.addChannelType(type.getValue());
        }
        return this;
    }

    /**
     * Set the minimum value accepted for this option. Only valid on INTEGER and NUMBER types.
     *
     * @param minValue the minimum value the user can provide for this option
     * @return this instance
     * @throws SlashLibOptionException when setting a min value for an option type that isn't INTEGER or NUMBER
     */
    public OptionBuilder setMinValue(double minValue) {
        if (!(this.type.equals(ApplicationCommandOption.Type.INTEGER) || this.type.equals(ApplicationCommandOption.Type.NUMBER))) {
            throw new SlashLibOptionException(this, "Can only have a minimum value on an INTEGER or NUMBER option.");
        }
        this.option.minValue(minValue);
        return this;
    }

    /**
     * Set the maximum value accepted for this option. Only valid on INTEGER and NUMBER types.
     *
     * @param maxValue the maximum value the user can provide for this option
     * @return this instance
     * @throws SlashLibOptionException when setting a max value for an option type that isn't INTEGER or NUMBER
     */
    public OptionBuilder setMaxValue(double maxValue) {
        if (!(this.type.equals(ApplicationCommandOption.Type.INTEGER) || this.type.equals(ApplicationCommandOption.Type.NUMBER))) {
            throw new SlashLibOptionException(this, "Can only have a maximum value on an INTEGER or NUMBER option.");
        }
        this.option.maxValue(maxValue);
        return this;
    }

    /**
     * Enable autocomplete events to be received when a user starts providing input for this option.
     * AutoComplete and Choices are mutually exclusive.
     *
     * @return this instance
     * @throws SlashLibOptionException when attempting to enable autocomplete when choices are set
     */
    public OptionBuilder setAutoCompleteEnabled() {
        if (this.choiceCount > 0) {
            throw new SlashLibOptionException(this, "Cannot have autocomplete enabled and choices!");
        }
        this.autocomplete = true;
        this.option.autocomplete(true);
        return this;
    }

    /**
     * Build a {@link ImmutableApplicationCommandOptionData} which can be provided to a callable command
     *  with {@link ChatCommand#addOption(ApplicationCommandOptionData)}.
     *
     * @return a newly created {@link ImmutableApplicationCommandOptionData} based off the data in this instance
     */
    public ImmutableApplicationCommandOptionData build() {
        return this.option.build();
    }
}
