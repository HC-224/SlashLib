package dev.hc224.slashlib.example.receiver;

import dev.hc224.slashlib.EventReceiverImpl;
import dev.hc224.slashlib.SlashLib;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.MessageContext;
import dev.hc224.slashlib.context.UserContext;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.event.domain.interaction.UserInteractionEvent;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

/**
 * A Custom event receiver which logs the name of the event to console with a custom prefix.
 */
public class CustomEventReceiver extends EventReceiverImpl {
    private static final Logger logger = Loggers.getLogger(CustomEventReceiver.class);

    private final String logPrefix;

    CustomEventReceiver(SlashLib slashLib, String logPrefix) {
        super(slashLib);
        this.logPrefix = logPrefix;
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent") // We will have the ACI name
    public Mono<ChatContext> receiveChatInputInteractionEvent(ChatInputInteractionEvent event) {
        return Mono.justOrEmpty(event.getInteraction().getCommandInteraction())
            .doOnNext(aci -> logger.info(logPrefix + "Received new CHAT_INPUT event: "
                + aci.getName().get()))
            .flatMap(aci -> super.receiveChatInputInteractionEvent(event)
                .doOnNext(context -> logger.info(logPrefix + "Finished handling CHAT_INPUT event: "
                    + aci.getName().get())));
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent") // We will have the ACI name
    public Mono<UserContext> receiveUserInteractionEvent(UserInteractionEvent event) {
        return Mono.justOrEmpty(event.getInteraction().getCommandInteraction())
            .doOnNext(aci -> logger.info(logPrefix + "Received new USER event: "
                + aci.getName().get()))
            .flatMap(aci -> super.receiveUserInteractionEvent(event)
                .doOnNext(context -> logger.info(logPrefix + "Finished handling USER event: "
                    + aci.getName().get())));
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent") // We will have the ACI name
    public Mono<MessageContext> receiveMessageInteractionEvent(MessageInteractionEvent event) {
        return Mono.justOrEmpty(event.getInteraction().getCommandInteraction())
            .doOnNext(aci -> logger.info(logPrefix + "Received new MESSAGE event: "
                + aci.getName().get()))
            .flatMap(aci -> super.receiveMessageInteractionEvent(event)
                .doOnNext(context -> logger.info(logPrefix + "Finished handling MESSAGE event: "
                    + aci.getName().get())));
    }
}
