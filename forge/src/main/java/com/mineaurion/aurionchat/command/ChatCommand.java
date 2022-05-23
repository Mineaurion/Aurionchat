package com.mineaurion.aurionchat.command;

import com.mineaurion.aurionchat.AurionChat;
import com.mineaurion.aurionchat.AurionChatPlayer;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class ChatCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSource>literal("channel")
                        .then(
                                Commands.argument("channel", StringArgumentType.string()).suggests(((context, builder) -> {
                                    AurionChat.config.getChannels().forEach((name, channel) -> builder.suggest(name));
                                    return builder.buildFuture();
                                }))
                                .then(
                                        Commands.literal("join")
                                                .executes(ctx -> join(ctx.getSource().getPlayerOrException(), StringArgumentType.getString(ctx, "channel")))
                                )
                                .then(
                                        Commands.literal("leave")
                                                .executes(ctx -> leave(ctx.getSource().getPlayerOrException(), StringArgumentType.getString(ctx, "channel")))
                                )
                                .then(
                                        Commands.literal("spy")
                                            .executes(ctx -> spy(ctx.getSource().getPlayerOrException(), StringArgumentType.getString(ctx, "channel")))
                                )
                        )
                        .then(
                                Commands.literal("allListen").executes(ctx -> allListen(ctx.getSource().getPlayerOrException()))
                        )
                        .executes(ctx -> defaultCommand(ctx.getSource().getPlayerOrException()))
        );
    }

    private static int join(ServerPlayerEntity player, String channel)
    {
        AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(player.getUUID());
        aurionChatPlayer.removeChannel(aurionChatPlayer.getCurrentChannel());
        aurionChatPlayer.setCurrentChannel(channel);
        player.sendMessage(new StringTextComponent("You have joined the " + channel + " channel.").withStyle(TextFormatting.GOLD), Util.NIL_UUID);
       return 1;
    }

    private static int leave(ServerPlayerEntity player, String channel)
    {
        AurionChat.aurionChatPlayers.get(player.getUUID()).removeChannel(channel);
        player.sendMessage(new StringTextComponent("You have leaved the " + channel + " channel.").withStyle(TextFormatting.GOLD), Util.NIL_UUID);
        return 1;
    }

    private static int spy(ServerPlayerEntity player, String channel)
    {
        AurionChat.aurionChatPlayers.get(player.getUUID()).addChannel(channel);
        player.sendMessage(new StringTextComponent("You have spy the " + channel + " channel.").withStyle(TextFormatting.GOLD), Util.NIL_UUID);
        return 1;
    }

    private static int allListen(ServerPlayerEntity player)
    {
        AurionChat.config.getChannels().forEach(((name, channel) -> spy(player, name)));
        return 1;
    }

    private static int defaultCommand(ServerPlayerEntity player)
    {
        AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(player.getUUID());
        StringBuilder channels = new StringBuilder();
        aurionChatPlayer.getChannels().forEach(name -> channels.append(name).append(" "));
        IFormattableTextComponent textComponent = new StringTextComponent("Your current channel: ").withStyle(TextFormatting.GRAY)
                .append(new StringTextComponent(aurionChatPlayer.getCurrentChannel()).withStyle(TextFormatting.WHITE))
                .append(new StringTextComponent("\nYou spying on channels : ").withStyle(TextFormatting.GRAY))
                .append(new StringTextComponent(channels.toString()).withStyle(TextFormatting.WHITE));
        player.sendMessage(textComponent, Util.NIL_UUID);
        return 1;
    }
}
