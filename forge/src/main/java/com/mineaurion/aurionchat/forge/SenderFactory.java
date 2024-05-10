package com.mineaurion.aurionchat.forge;

import com.mineaurion.aurionchat.api.model.ServerPlayer;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.rcon.RconConsoleSource;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

import static com.mineaurion.aurionchat.forge.AurionChat.toNativeText;

public class SenderFactory extends com.mineaurion.aurionchat.common.command.sender.SenderFactory<AurionChat, CommandSourceStack> {

    public SenderFactory(AurionChat plugin){
        super(plugin, true);
    }

    @Override
    protected UUID getId(CommandSourceStack commandSource) {
        if(commandSource.getEntity() instanceof Player){
            return commandSource.getEntity().getUUID();
        }
        return ServerPlayer.CONSOLE_UUID;
    }

    @Override
    protected String getName(CommandSourceStack commandSource) {
        if(commandSource.getEntity() instanceof Player){
            return commandSource.getTextName();
        }
        return ServerPlayer.CONSOLE_NAME;
    }

    // TODO: need to be tested
    @Override
    protected String getDisplayName(CommandSourceStack sender) {
        return sender.getDisplayName().getString();
    }

    @Override
    protected void sendMessage(CommandSourceStack sender, Component message) {
        sender.sendSuccess(() -> toNativeText(message), false);
    }

    @Override
    protected boolean hasPermission(CommandSourceStack sender, String permission) {
        // If admin permission we check if the sender is OP
        return permission.contains("admin") ? sender.hasPermission(4) : sender.hasPermission(0);
    }

    @Override
    protected boolean isConsole(CommandSourceStack sender) {
        CommandSource output = sender.source;
        return output == sender.getServer() || // Console
                output.getClass() == RconConsoleSource.class || // Rcon
                (output == CommandSource.NULL && sender.getTextName().equals("")); // Functions

    }


}