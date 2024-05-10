package com.mineaurion.aurionchat.forge;

import com.mineaurion.aurionchat.api.model.ServerPlayer;
import com.mineaurion.aurionchat.common.command.BrigadierCommandManager;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandExecutor extends BrigadierCommandManager<CommandSourceStack> {

    private final AurionChat plugin;

    public CommandExecutor(AurionChat plugin){
        super(plugin);
        this.plugin = plugin;
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event){
        for(String alias: COMMAND_ALIASES){
            LiteralCommandNode<CommandSourceStack> command = Commands.literal(alias).executes(this).build();
            ArgumentCommandNode<CommandSourceStack, String> arguments = Commands.argument("args", StringArgumentType.greedyString())
                    .suggests(this)
                    .executes(this)
                    .build();
            command.addChild(arguments);
            event.getDispatcher().getRoot().addChild(command);
        }
    }

    @Override
    public ServerPlayer getSender(CommandSourceStack source) {
        return this.plugin.getSenderFactory().wrap(source);
    }

}