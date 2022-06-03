package com.mineaurion.aurionchat.forge.command;

import com.mineaurion.aurionchat.common.command.ChatCommandCommon;
import com.mineaurion.aurionchat.forge.AurionChat;
import com.mineaurion.aurionchat.forge.AurionChatPlayer;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class ChatCommand extends ChatCommandCommon<AurionChatPlayer> {
    public ChatCommand(CommandDispatcher<CommandSource> dispatcher){
        super(AurionChat.config.getChannels().keySet());
        this.register(dispatcher);
    }
    public void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSource>literal("channel")
                        .then(
                                Commands.argument("channel", StringArgumentType.string()).suggests(((context, builder) -> {
                                    AurionChat.config.getChannels().forEach((name, channel) -> builder.suggest(name));
                                    return builder.buildFuture();
                                }))
                                .then(Commands.literal("join").executes(ctx -> this.execute(ctx, Action.JOIN)))
                                .then(Commands.literal("leave").executes(ctx -> this.execute(ctx, Action.LEAVE)))
                                .then(Commands.literal("spy").executes(ctx -> this.execute(ctx, Action.SPY)))
                        )
                        .then(Commands.literal("allListen").executes(ctx -> this.execute(ctx, Action.ALLLISTEN)))
                        .executes(ctx -> this.execute(ctx, Action.DEFAULT))
        );
    }

    private int execute(CommandContext<CommandSource> ctx, Action action) {
        try {
            ServerPlayerEntity player = ctx.getSource().getPlayerOrException();
            AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(player.getUUID());
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