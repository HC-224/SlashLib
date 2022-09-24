package dev.hc224.slashlib;

import dev.hc224.slashlib.context.*;

/**
 * A wrapper for the {@link GenericEventReceiver} to aid with easily extending/altering
 *  the provided event receiver logic.
 */
public class EventReceiverImpl
        extends GenericEventReceiverImpl<
        ChatContext, ChatContextBuilder,
        UserContext, UserContextBuilder,
        MessageContext, MessageContextBuilder
        > {

    public EventReceiverImpl(SlashLib slashLib) {
        super(slashLib);
    }
}
