package dev.hc224.slashlib.example.basic.interactions.chat.choices;

import dev.hc224.slashlib.commands.standard.SubCommand;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.utility.OptionBuilder;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

/**
 * An example command which demonstrates using choices for option values.
 */
public class ChoicesColors extends SubCommand {
    public ChoicesColors() {
        super("colors", "a command with an option that accepts pre-defined colors as the value");
        addOption(OptionBuilder.requiredInteger("color", "a selected color")
                .addChoice("red", 0xFF0000)
                .addChoice("green", 0x00FF00)
                .addChoice("blue", 0x0000FF)
            .build());
    }

    @Override
    public Mono<ChatContext> executeChat(ChatContext context) {
        // the color option is required and will be present
        //noinspection OptionalGetWithoutIsPresent
        return context.getEvent().reply(InteractionApplicationCommandCallbackSpec.builder()
            .addEmbed(EmbedCreateSpec.builder()
                .color(Color.of(Math.toIntExact(context.getOptions().getInteger("color").get())))
                .description("Color Value: `" + context.getOptions().getInteger("color").get() + "`")
                .build())
            .build())
            .thenReturn(context);
    }
}
