package dev.hc224.slashlib.example.response;

import dev.hc224.slashlib.example.response.slashlib.context.ResponseChatContext;
import dev.hc224.slashlib.example.response.slashlib.context.ResponseMessageContext;
import dev.hc224.slashlib.example.response.slashlib.context.ResponseUserContext;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.core.spec.MessageCreateFields;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * A class which is used to set the final state of an executing command.
 * Includes the state of the command, and content/embeds/attachments to be included with the response.
 * This class stores message properties which are used to build various responses to different events,
 *  as the responses to CHAT_INPUT and MESSAGE/USER interactions are different.
 *
 * This class is included in context classes for commands to set the state and various message parts
 *  used when responding to the calling user.
 * {@link ResponseChatContext}
 * {@link ResponseUserContext}
 * {@link ResponseMessageContext}
 *
 * Not all methods in this class are tested/used in this example.
 */
public class CommandResponse {
    // Command Execution State
    private CommandState commandState;

    // Message Response Properties
    private String content;
    // The embeds to attach
    private List<EmbedCreateSpec> embeds;
    // The files to attach
    private List<MessageCreateFields.File> files;
    // The files to attach as spoilers
    private List<MessageCreateFields.FileSpoiler> fileSpoilers;
    // If the response should be ephemeral (private) when responding to an interaction
    private boolean ephemeral;

    public CommandResponse() {
        commandState = CommandState.UNKNOWN;

        content = "";
        embeds = new ArrayList<>();
        files = new ArrayList<>();
        fileSpoilers = new ArrayList<>();
        ephemeral = false;
    }

    /**
     * Set the final state of the command to determine response behavior.
     *
     * @param commandState the state provided by the command, or from an error
     * @return this instance modified
     */
    public CommandResponse setState(CommandState commandState) {
        this.commandState = commandState;
        return this;
    }

    /**
     * Set the content to be provided with the response message.
     *
     * @param content content of the response message
     * @return this instance modified
     */
    public CommandResponse setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Append additional content to the existing content for this response.
     * The default content is empty, this can be used in place of
     *  {@link CommandResponse#setContent(String)}
     *
     * @param content additional content to append to the existing content
     * @return this instance modified
     */
    public CommandResponse appendContent(String content) {
        this.content += content;
        return this;
    }

    /**
     * Add an embed to be sent with the response.
     * This will set the color if not provided.
     *
     * @param embed the embed to send with the response
     * @return this instance modified
     */
    public CommandResponse addEmbed(EmbedCreateSpec.Builder embed) {
        // Set the color if not present, a rather expensive operation
        //  for what should be a boolean comparison ...
        if (!embed.build().isColorPresent()) {
            embed.color(Color.CYAN);
        }

        // Fail fast if we try to attach more embeds than we can.
        // Consider a custom exception for clarity.
        if (this.embeds.size() >= 10) {
            throw new RuntimeException("Exceeded the number of embeds that can be on a message!");
        }

        this.embeds.add(embed.build());
        return this;
    }

    /**
     * Set if the response should be ephemeral (private) or not. Default false.
     *
     * @param ephemeral if the response should be ephemeral
     * @return this instance modified
     */
    public CommandResponse setIsEphemeral(boolean ephemeral) {
        this.ephemeral = ephemeral;
        return this;
    }

    /**
     * Set multiple properties of this instance to respond with an error message.
     *
     * @param throwable the unhandled throwable from command execution
     * @return this instance modified
     */
    public CommandResponse setThrowable(Throwable throwable) {
        this.setState(CommandState.ERROR);
        this.content = "An unexpected error occurred during execution!";
        // Clear embeds and files
        this.embeds = new ArrayList<>();
        this.files = new ArrayList<>();
        this.fileSpoilers = new ArrayList<>();
        this.ephemeral = true;
        // It is a huge security issue to include the throwable message in the response.
        // Some exceptions (like with JDBI/SQL) will include schema information.
        // While the throwable isn't included, some information from it could be desirable.
        // Such as the class name if using custom exceptions.
        return this;
    }

    /**
     * Set multiple properties of this instance to respond with a warning message.
     *
     * @return this instance modified
     */
    public CommandResponse checkUnknown() {
        if (commandState.equals(CommandState.UNKNOWN)) {
            this.content = "The command finished in an unknown state!";
            // Clear embeds and files
            this.embeds = new ArrayList<>();
            this.files = new ArrayList<>();
            this.fileSpoilers = new ArrayList<>();
            this.ephemeral = true;
        }
        return this;
    }

    /**
     * @return an un-built {@link MessageCreateSpec.Builder} containing all the data set in this instance
     */
    public MessageCreateSpec.Builder asMessage() {
        return MessageCreateSpec.builder()
            .content(content)
            .embeds(embeds)
            .files(files)
            .fileSpoilers(fileSpoilers);
    }

    /**
     * @return a callback spec that can be used to respond to interactions with
     */
    public InteractionApplicationCommandCallbackSpec asInteractionCallback() {
        return InteractionApplicationCommandCallbackSpec.builder()
            .content(content)
            .embeds(embeds)
            .ephemeral(ephemeral)
            .build();
        // Interactions cannot reply with files
    }

    /** @return the current state of the command */
    public CommandState getCommandState() { return commandState; }
}