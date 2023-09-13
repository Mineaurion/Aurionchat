package com.mineaurion.aurionchat.sponge.command;

import com.mineaurion.aurionchat.common.command.ChatCommandCommon;
import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.AurionChatPlayer;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;

public class ChatCommand extends ChatCommandCommon {

    private final AurionChat plugin;

    Parameter.Value<String> channel = Parameter.string().key("channel").build();

    public ChatCommand(AurionChat plugin, RegisterCommandEvent<Command.Parameterized> commandManager){
        super(AurionChat.config.channels.keySet());
        this.plugin = plugin;
        register(commandManager);
        registerAliasChannels(commandManager);
    }

    private void register(RegisterCommandEvent<Command.Parameterized> commandManager){


        Command.Parameterized join = Command.builder()
                .permission("aurionchat.command.channel")
                .addParameter(channel)
                .executor(ctx -> this.execute(ctx, Action.JOIN))
                .build();

        Command.Parameterized leave = Command.builder()
                .permission("aurionchat.command.channel")
                .addParameter(channel)
                .executor(ctx -> this.execute(ctx, Action.LEAVE))
                .build();

        Command.Parameterized spy = Command.builder()
                .permission("aurionchat.command.channel")
                .addParameter(channel)
                .executor(ctx -> this.execute(ctx, Action.SPY))
                .build();

        Command.Parameterized allListen = Command.builder()
                .permission("aurionchat.command.channel")
                .executor(ctx -> this.execute(ctx, Action.ALLLISTEN))
                .build();

        Command.Parameterized reload = Command.builder()
                .permission("aurionchat.reload")
                .shortDescription(Component.text("AurionChat's reload command"))
                .executor(ctx -> this.execute(ctx, Action.RELOAD))
                .build();

        Command.Parameterized cmdChat = Command.builder()
                .shortDescription(Component.text("AurionChat's command to manage chat channels"))
                .executionRequirements(context -> context.cause().root() instanceof ServerPlayer)
                .addChild(join, "join")
                .addChild(leave, "leave")
                .addChild(spy, "spy")
                .addChild(allListen, "allListen")
                .addChild(reload, "reload")
                .executor(ctx -> this.execute(ctx, Action.DEFAULT))
                .build();


        commandManager.register(this.plugin.container, cmdChat,"channel", "ch");
    }

    private void registerAliasChannels(RegisterCommandEvent<Command.Parameterized> commandManager){
        Parameter.Value<String> messageParameter = Parameter.remainingJoinedStrings().optional().key("message").build();
        AurionChat.config.channels.forEach((name, channel) -> {
            Command.Parameterized aliasCmd = Command.builder()
                    .shortDescription(Component.text("AurionChat's command to send message on specific channel"))
                    .addParameter(Parameter.remainingJoinedStrings().optional().key("message").build())
                    .executionRequirements(context -> context.cause().root() instanceof ServerPlayer)
                    .executor(ctx -> {
                        ServerPlayer player = (ServerPlayer) ctx.cause().root();
                        return onCommand(
                                this.plugin.getAurionChatPlayers().get(player.uniqueId()),
                                Component.text(ctx.requireOne(messageParameter)),
                                name,
                                channel.format
                        ) ? CommandResult.success() :CommandResult.error(Component.text("Error when executing the command"));
                    }).build();
            commandManager.register(this.plugin.container, aliasCmd, channel.alias, name);
        });
    }

    public CommandResult execute(CommandContext context, Action action) {
        String channel  = context.requireOne(this.channel);
        ServerPlayer player = (ServerPlayer) context.cause().root();
        AurionChatPlayer aurionChatPlayer = this.plugin.getAurionChatPlayers().get(player.uniqueId());
        return this.execute(aurionChatPlayer, channel, action) ? CommandResult.success() : CommandResult.error(Component.text("Error when executing the command"));
    }
}
