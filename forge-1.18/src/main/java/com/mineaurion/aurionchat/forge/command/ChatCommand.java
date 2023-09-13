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
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;


public class ChatCommand extends ChatCommandCommon {

    private final AurionChat plugin;

    public ChatCommand(AurionChat plugin, CommandDispatcher<CommandSourceStack> dispatcher){
        super(AurionChat.config.getChannels().keySet());
        this.plugin = plugin;
        register(dispatcher);
        registerAliasChannels(dispatcher);
    }

    public void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        RequiredArgumentBuilder<CommandSourceStack, String> channelArg = Commands.argument("channel", StringArgumentType.string()).suggests(((context, builder) -> {
            AurionChat.config.getChannels().forEach((name, channel) -> builder.suggest(name));
            return builder.buildFuture();
        }));

        LiteralCommandNode<CommandSourceStack> join = Commands.literal("join")
                .then(channelArg.executes(ctx -> this.execute(ctx, Action.JOIN)))
                .build();

        LiteralCommandNode<CommandSourceStack> leave = Commands.literal("leave")
                .then(channelArg.executes(ctx -> this.execute(ctx, Action.LEAVE)))
                .build();

        LiteralCommandNode<CommandSourceStack> spy = Commands.literal("spy")
                .then(channelArg.executes(ctx -> this.execute(ctx, Action.SPY)))
                .build();

        LiteralCommandNode<CommandSourceStack> allListen = Commands.literal("allListen")
                .executes(ctx -> this.execute(ctx, Action.ALLLISTEN))
                .build();

        LiteralCommandNode<CommandSourceStack> reload = Commands.literal("reload")
                .requires((commandSource) -> commandSource.hasPermission(3))
                .executes(ctx -> this.execute(ctx, Action.RELOAD))
                .build();


        dispatcher.register(
          Commands.literal("channel")
                  .then(join)
                  .then(leave)
                  .then(spy)
                  .then(allListen)
                  .then(reload)
                  .executes(ctx -> this.execute(ctx, Action.DEFAULT))
        );
    }

    public void registerAliasChannels(CommandDispatcher<CommandSourceStack> dispatcher){
        RequiredArgumentBuilder<CommandSourceStack, MessageArgument.Message> messageArg = Commands.argument("message", MessageArgument.message());

        AurionChat.config.getChannels().forEach((name, channel) -> {
            ArgumentBuilder<CommandSourceStack, RequiredArgumentBuilder<CommandSourceStack, MessageArgument.Message>> argBuilder = messageArg.executes(ctx -> (onCommand(
                    this.plugin.getAurionChatPlayers().get(ctx.getSource().getPlayerOrException().getUUID()),
                    GsonComponentSerializer.gson().deserialize(Component.Serializer.toJson(MessageArgument.getMessage(ctx, "message"))),
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

    private int execute(CommandContext<CommandSourceStack> ctx, Action action) {
        try {
            ServerPlayer player = ctx.getSource().getPlayerOrException();
            AurionChatPlayer aurionChatPlayer = this.plugin.getAurionChatPlayers().get(player.getUUID());
            String channel;
            try{
                channel = StringArgumentType.getString(ctx, "channel");
            } catch (IllegalArgumentException e){
                channel = "";
            }
            return this.execute(aurionChatPlayer, channel , action) ? 1 : 0;
        } catch (CommandSyntaxException e){
            ctx.getSource().sendFailure(new TextComponent("You need to be a player to do this"));
            return 0;
        }
    }
}