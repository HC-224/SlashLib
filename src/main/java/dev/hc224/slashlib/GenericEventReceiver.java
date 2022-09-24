package dev.hc224.slashlib;

import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.MessageContext;
import dev.hc224.slashlib.context.UserContext;
import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.event.domain.interaction.UserInteractionEvent;
import reactor.core.publisher.Mono;

/**
 * The Core event receiver interface.
 *
 * @param <CI> the {@link ChatContext} to provide to chat commands
 * @param <UI> the {@link UserContext} to provide to user commands
 * @param <MI> the {@link MessageContext} to provide to message commands
 */
public interface GenericEventReceiver<CI extends ChatContext, UI extends UserContext, MI extends MessageContext> {
    Mono<CI> receiveChatInputInteractionEvent(ChatInputInteractionEvent event);
    Mono<UI> receiveUserInteractionEvent(UserInteractionEvent event);
    Mono<MI> receiveMessageInteractionEvent(MessageInteractionEvent event);
    Mono<Void> receiveAutoCompleteEvent(ChatInputAutoCompleteEvent event);
}
