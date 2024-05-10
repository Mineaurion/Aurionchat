package com.mineaurion.aurionchat.fabric;

import com.mineaurion.aurionchat.api.model.ServerPlayer;
import com.mineaurion.aurionchat.fabric.mixin.ServerCommandSourceAccessor;
import net.kyori.adventure.text.Component;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.rcon.RconCommandOutput;

import java.util.UUID;

import static com.mineaurion.aurionchat.fabric.AurionChat.toNativeText;

public class SenderFactory extends com.mineaurion.aurionchat.common.command.sender.SenderFactory<AurionChat, ServerCommandSource> {

    public SenderFactory(AurionChat plugin){
        super(plugin, true);
    }

    @Override
    protected UUID getId(ServerCommandSource sender) {
        if(sender.getEntity() != null){
            return sender.getEntity().getUuid();
        }
        return ServerPlayer.CONSOLE_UUID;
    }

    @Override
    protected String getName(ServerCommandSource sender) {
        String name = sender.getName();
        if(sender.getEntity() != null && name.equals("Server")){
            return ServerPlayer.CONSOLE_NAME;
        }
        return name;
    }

    // TODO: need to be tested
    @Override
    protected String getDisplayName(ServerCommandSource sender) {
        return sender.getDisplayName().getString();
    }

    @Override
    protected void sendMessage(ServerCommandSource sender, Component message) {
        sender.sendFeedback(() -> toNativeText(message), false);
    }

    @Override
    protected boolean hasPermission(ServerCommandSource sender, String permission) {
        return permission.contains("admin") ? sender.hasPermissionLevel(4) : sender.hasPermissionLevel(0);
    }

    @Override
    protected boolean isConsole(ServerCommandSource sender) {
        CommandOutput output = ((ServerCommandSourceAccessor) sender).getOutput();
        return output == sender.getServer() || // console
                output.getClass() == RconCommandOutput.class || // Rcon
                (output == CommandOutput.DUMMY && sender.getName().equals("")); // Functions
    }
}