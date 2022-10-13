package dev.hc224.slashlib.example.basic.interactions.chat;

import dev.hc224.slashlib.commands.standard.TopCommand;
import dev.hc224.slashlib.context.ChatContext;
import dev.hc224.slashlib.context.ChatContextBuilder;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

/**
 * An example command which requires the bot and calling user to have the `BAN_MEMBERS` permission.
 * This won't actually ban anyone, it is used to demo/test the permissions checking logic.
 * <p>
 * This version will enforce checking the users permissions.
 */
public class TestBanEnforce extends TopCommand {
    public TestBanEnforce() {
        super("test-ban-enforce", "check the permissions needed to ban a user, enforces permissions");
        setBotPermissions(Permission.BAN_MEMBERS);
        setDefaultMemberPermissions(Permission.BAN_MEMBERS);
        setEnforceMemberPermission();
    }

    @Override
    public Mono<ChatContext> executeChat(ChatContext context) {
        // The permission check is handled by the event receiver logic
        // This will only be called if all permissions are present

        // We have required the bot user be present
        //noinspection OptionalGetWithoutIsPresent
        return context.getEvent().reply("Both " + context.getBotUser().get().getUsername() +
            " and " + context.getUser().getUsername() + " have the `BAN_MEMBERS` permission.")
            .thenReturn(context);
    }

    @Override
    public ChatContextBuilder setRequestData(ChatContextBuilder contextBuilder) {
        return (ChatContextBuilder) contextBuilder.requireBotUser();
    }
}
