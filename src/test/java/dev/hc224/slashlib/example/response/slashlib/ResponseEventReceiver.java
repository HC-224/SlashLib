package dev.hc224.slashlib.example.response.slashlib;

import dev.hc224.slashlib.GenericEventReceiverImpl;
import dev.hc224.slashlib.GenericSlashLib;
import dev.hc224.slashlib.example.response.CommandResponse;
import dev.hc224.slashlib.example.response.slashlib.context.*;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.event.domain.interaction.UserInteractionEvent;
import reactor.core.publisher.Mono;

/**
 * An extension of the {@link GenericEventReceiverImpl} which adds additional functionality
 *  to get the {@link CommandResponse} added to the
 *  context and responds to the interaction with its contents.
 */
public class ResponseEventReceiver extends GenericEventReceiverImpl<
        ResponseChatContext, ResponseChatContextBuilder,
        ResponseUserContext, ResponseUserContextBuilder, ResponseMessageContext, ResponseMessageContextBuilder
        > {
    public ResponseEventReceiver(GenericSlashLib<
            ResponseChatContext, ResponseChatContextBuilder,
            ResponseUserContext, ResponseUserContextBuilder, ResponseMessageContext, ResponseMessageContextBuilder
            > genericSlashLib) {
        super(genericSlashLib);
    }

    @Override
    public Mono<ResponseChatContext> receiveChatInputInteractionEvent(ChatInputInteractionEvent event) {
        // Use the existing default logic to call the command
        return super.receiveChatInputInteractionEvent(event)
            // We check if we have an unknown state, which will properly set the state for an ephemeral warning message.
            // This also changes the return type to CommandResponse,
            .map(context -> context.getCommandResponse().checkUnknown())
            // We can catch unhandled errors here and set the response to state them
            .onErrorResume(t -> Mono.just(new CommandResponse().setThrowable(t)))
            // Reply to the event with the response
            // Empty is ignored as it is used in the default logic to reply without executing a command
            .flatMap(response -> event.reply(response.asInteractionCallback()))
            // The default behavior returns the context class so that additional functionality can be
            //  appended to it like what we are doing here. But since we don't do anything with the
            //  context class after this we can safely go empty.
            // This line is also needed for type compatibility.
            .then(Mono.empty());
    }

    @Override
    public Mono<ResponseUserContext> receiveUserInteractionEvent(UserInteractionEvent event) {
        return super.receiveUserInteractionEvent(event)
            .map(context -> context.getCommandResponse().checkUnknown())
            .onErrorResume(t -> Mono.just(new CommandResponse().setThrowable(t)))
            .flatMap(response -> event.reply(response.asInteractionCallback()))
            .then(Mono.empty());
    }

    @Override
    public Mono<ResponseMessageContext> receiveMessageInteractionEvent(MessageInteractionEvent event) {
        return super.receiveMessageInteractionEvent(event)
            .map(context -> context.getCommandResponse().checkUnknown())
            .onErrorResume(t -> Mono.just(new CommandResponse().setThrowable(t)))
            .flatMap(response -> event.reply(response.asInteractionCallback()))
            .then(Mono.empty());
    }
}
