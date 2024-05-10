package com.mineaurion.aurionchat.fabric;

import com.mineaurion.aurionchat.api.model.ServerPlayer;
import com.mineaurion.aurionchat.common.command.BrigadierCommandManager;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class CommandExecutor extends BrigadierCommandManager<ServerCommandSource> {

    private final AurionChat plugin;

    public CommandExecutor(AurionChat plugin){
        super(plugin);
        this.plugin = plugin;
    }

    public void register(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            for(String alias: COMMAND_ALIASES){
                LiteralCommandNode<ServerCommandSource> command = literal(alias).executes(this).build();

                ArgumentCommandNode<ServerCommandSource, String> arguments = argument("args", greedyString())
                        .suggests(this)
                        .executes(this)
                        .build();
                command.addChild(arguments);
                dispatcher.getRoot().addChild(command);
            }
        });
    }

    @Override
    public ServerPlayer getSender(ServerCommandSource source) {
        return this.plugin.getSenderFactory().wrap(source);
    }
}