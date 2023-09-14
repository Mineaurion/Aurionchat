package com.mineaurion.aurionchat.fabric.command;

import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.command.ChatCommandCommon;
import com.mineaurion.aurionchat.fabric.AurionChat;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ChatCommand extends ChatCommandCommon {

    private final AurionChat plugin;
    public ChatCommand(AurionChat plugin, CommandDispatcher<ServerCommandSource> dispatcher) {
        super(plugin.getConfigurationAdapter().getChannels().keySet());
        this.plugin = plugin;
        register(dispatcher);
        registerAliasChannels(dispatcher);
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher){
        RequiredArgumentBuilder<ServerCommandSource, String> channelArg = CommandManager.argument("channel", StringArgumentType.string()).suggests((context, builder) -> {
            plugin.getConfigurationAdapter().getChannels().forEach((name, chanel) -> builder.suggest(name));
            return builder.buildFuture();
        });

        LiteralCommandNode<ServerCommandSource> join = CommandManager.literal("join")
                .then(channelArg.executes(ctx -> this.execute(ctx, Action.JOIN)))
                .build();

        LiteralCommandNode<ServerCommandSource> leave = CommandManager.literal("leave")
                .then(channelArg.executes(ctx -> this.execute(ctx, Action.LEAVE)))
                .build();

        LiteralCommandNode<ServerCommandSource> spy = CommandManager.literal("spy")
                .then(channelArg.executes(ctx -> this.execute(ctx, Action.SPY)))
                .build();

        LiteralCommandNode<ServerCommandSource> allListen = CommandManager.literal("allListen")
                .executes(ctx -> this.execute(ctx, Action.ALLLISTEN))
                .build();

        LiteralCommandNode<ServerCommandSource> reload = CommandManager.literal("reload")
                .requires(commandSource -> commandSource.hasPermissionLevel(3))
                .then(channelArg.executes(ctx -> this.execute(ctx, Action.RELOAD)))
                .build();

        dispatcher.register(
            CommandManager.literal("channel")
                    .then(join)
                    .then(leave)
                    .then(spy)
                    .then(allListen)
                    .then(reload)
                    .executes(ctx -> this.execute(ctx, Action.DEFAULT))
        );
    }

    private int execute(CommandContext<ServerCommandSource> ctx, Action action){
        try {
            ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
            AurionChatPlayer aurionChatPlayer = this.plugin.getAurionChatPlayers().get(player.getUuid());
            String channel;
            try {
                channel = StringArgumentType.getString(ctx, "channel");
            } catch (IllegalArgumentException e) {
                channel = "";
            }
            return this.execute(aurionChatPlayer, channel, action) ? 1 : 0;
        } catch (CommandSyntaxException e) {
            ctx.getSource().sendError(Text.of("You need to be a player to do this"));
            return 0;
        }
    }

    private void registerAliasChannels(CommandDispatcher<ServerCommandSource> dispatcher){
        RequiredArgumentBuilder<ServerCommandSource, MessageArgumentType.MessageFormat> messageArg = CommandManager.argument("message", MessageArgumentType.message());
        plugin.getConfigurationAdapter().getChannels().forEach((name, channel) -> {
            ArgumentBuilder<ServerCommandSource, RequiredArgumentBuilder<ServerCommandSource, MessageArgumentType.MessageFormat>> argBuilder = messageArg.executes(ctx -> (onCommand(
                    this.plugin.getAurionChatPlayers().get(ctx.getSource().getPlayer().getUuid()),
                    GsonComponentSerializer.gson().deserialize(Text.Serializer.toJson(MessageArgumentType.getMessage(ctx, "message"))),
                    name,
                    channel.format)) ? 1 : 0
            );
            dispatcher.register(
                    CommandManager.literal(channel.alias).requires(ServerCommandSource::isExecutedByPlayer).then(argBuilder)
            );
            dispatcher.register(
                    CommandManager.literal(name).requires(ServerCommandSource::isExecutedByPlayer).then(argBuilder)
            );
        });
    }
}
