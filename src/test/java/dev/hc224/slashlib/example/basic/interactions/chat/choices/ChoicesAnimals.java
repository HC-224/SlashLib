package dev.hc224.slashlib.example.basic.interactions.chat.choices;

import dev.hc224.slashlib.commands.standard.SubCommand;
import dev.hc224.slashlib.context.AutoCompleteContext;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.utility.OptionBuilder;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An example command which demonstrates autocomplete event receiving.
 *
 * Since there are many ways to retrieve many kinds of data (particularly generated or persistent data),
 *  the event is delivered directly to the command.
 */
public class ChoicesAnimals extends SubCommand {
    private final List<String> animals;

    public ChoicesAnimals() {
        super("animals", "a command with an option that tries to autocomplete for the user");
        addOption(OptionBuilder.requiredString("animal", "the name of an animal").setAutoCompleteEnabled().build());

        animals = new ArrayList<>();
        animals.addAll(Arrays.asList( // 5 entries per line, providing 30 which is 5 more than the limit for choices
            "aardvark", "buffalo", "elephant", "penguin", "turtle",
            "tortoise", "dog", "cat", "bunny", "rabbit",
            "ibex", "monkey", "alligator", "wolf", "badger",
            "ant", "armadillo", "cockatoo", "bird", "snake",
            "lion", "cheetah", "beetle", "pelican", "goose",
            "possum", "fish", "squid", "ferret", "rhino"));
    }

    @Override
    public Mono<ChatContext> executeChat(ChatContext context) {
        // the animal option is required and will be present
        //noinspection OptionalGetWithoutIsPresent
        return context.getEvent().reply(context.getOptions().getString("animal").get())
            .thenReturn(context);
    }

    @Override
    public Mono<Void> receiveAutoCompleteEvent(AutoCompleteContext event) {
        // Getting a method reference from a map with the option name as key
        //  could do well for multiple autocomplete options.
        return Mono.justOrEmpty(event.getOptions().getFocusedOption())
            // Not necessary in this example, but when multiple options
            //  can autocomplete something must filter them.
            .filter(option -> option.getName().equals("animal"))
            .map(option -> animals.stream()
                .filter(animal -> animal.toLowerCase().startsWith(event.getOptions().getString("animal").get()))
                // This is weird, we need the interface not the implementing class.
                .map(result -> (ApplicationCommandOptionChoiceData) ApplicationCommandOptionChoiceData.builder().name(result).value(result).build())
                .collect(Collectors.toList()))
            .flatMap(results -> event.getEvent().respondWithSuggestions(results));

    }
}
