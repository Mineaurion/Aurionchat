package com.mineaurion.aurionchat.forge.command;

import com.mineaurion.aurionchat.common.command.ChatCommandCommon;
import com.mineaurion.aurionchat.forge.AurionChat;
import com.mineaurion.aurionchat.forge.AurionChatPlayer;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class ChatCommand extends ChatCommandCommon {

    private final AurionChat plugin;

    public ChatCommand(AurionChat plugin, CommandDispatcher<CommandSource> dispatcher){
        super(AurionChat.config.getChannels().keySet());
        this.plugin = plugin;
        register(dispatcher);
        registerAliasChannels(dispatcher);
    }
    public void register(CommandDispatcher<CommandSource> dispatcher)
    {
        RequiredArgumentBuilder<CommandSource, String> channelArg = Commands.argument("channel", StringArgumentType.string()).suggests(((context, builder) -> {
            AurionChat.config.getChannels().forEach((name, channel) -> builder.suggest(name));
            return builder.buildFuture();
        }));

        LiteralCommandNode<CommandSource> join = Commands.literal("join")
                .then(channelArg.executes(ctx -> execute(ctx, Action.JOIN)))
                .build();

        LiteralCommandNode<CommandSource> leave = Commands.literal("leave")
                .then(channelArg.executes(ctx -> execute(ctx, Action.LEAVE)))
                .build();

        LiteralCommandNode<CommandSource> spy = Commands.literal("spy")
                .then(channelArg.executes(ctx -> execute(ctx, Action.SPY)))
                .build();

        LiteralCommandNode<CommandSource> allListen = Commands.literal("allListen")
                .executes(ctx -> execute(ctx, Action.ALLLISTEN))
                .build();

        LiteralCommandNode<CommandSource> reload = Commands.literal("reload")
                .requires((commandSource) -> commandSource.hasPermission(3))
                .executes(ctx -> execute(ctx, Action.RELOAD))
                .build();

        dispatcher.register(
                Commands.literal("channel")
                        .then(join)
                        .then(leave)
                        .then(spy)
                        .then(allListen)
                        .then(reload)
                        .executes(ctx -> execute(ctx, Action.DEFAULT))
        );
    }

    public void registerAliasChannels(CommandDispatcher<CommandSource> dispatcher){
        RequiredArgumentBuilder<CommandSource, MessageArgument.Message> messageArg = Commands.argument("message", MessageArgument.message());

        AurionChat.config.getChannels().forEach((name, channel) -> {
            ArgumentBuilder<CommandSource, RequiredArgumentBuilder<CommandSource, MessageArgument.Message>> argBuilder = messageArg.executes(ctx -> (onCommand(
                    this.plugin.getAurionChatPlayers().get(ctx.getSource().getPlayerOrException().getUUID()),
                    MessageArgument.getMessage(ctx, "message").getString(),
                    name,
                    channel.format)) ? 1 : 0
            );
            dispatcher.register(
                    Commands.literal(channel.alias).requires(commandSource -> commandSource.getEntity() != null).then(argBuilder)
            );
            dispatcher.register(
                    Commands.literal(name).requires(commandSource -> commandSource.getEntity() != null).then(argBuilder)
            );
        });
    }

    private int execute(CommandContext<CommandSource> ctx, Action action) {
        try {
            ServerPlayerEntity player = ctx.getSource().getPlayerOrException();
            AurionChatPlayer aurionChatPlayer = plugin.getAurionChatPlayers().get(player.getUUID());
            String channel;
            try{
                channel = StringArgumentType.getString(ctx, "channel");
            } catch (IllegalArgumentException e){
                channel = "";
            }
            return this.execute(aurionChatPlayer, channel , action) ? 1 : 0;
        } catch (CommandSyntaxException e){
            ctx.getSource().sendFailure(new StringTextComponent("You need to be a player to do this"));
            return 0;
        }
    }
}