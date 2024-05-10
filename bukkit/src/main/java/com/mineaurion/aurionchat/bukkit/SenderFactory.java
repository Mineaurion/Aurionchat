package com.mineaurion.aurionchat.bukkit;


import com.mineaurion.aurionchat.api.model.ServerPlayer;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SenderFactory extends com.mineaurion.aurionchat.common.command.sender.SenderFactory<AurionChat, CommandSender> {

    private final BukkitAudiences audiences;

    public SenderFactory(AurionChat plugin){
        super(plugin, true);
        this.audiences = plugin.getAudiences();
    }

    @Override
    protected String getName(CommandSender sender) {
        return sender instanceof Player ? sender.getName() : ServerPlayer.CONSOLE_NAME;
    }

    // TODO: need to check this
    @Override
    protected String getDisplayName(CommandSender sender) {
        return sender.getName();
    }

    @Override
    protected UUID getId(CommandSender sender) {
        return sender instanceof Player ? ((Player) sender).getUniqueId() : ServerPlayer.CONSOLE_UUID;
    }

    @Override
    protected void sendMessage(CommandSender sender, Component message) {
        this.audiences.sender(sender).sendMessage(message);
    }

    @Override
    protected boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    protected boolean isConsole(CommandSender sender) {
        return sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender;
    }

    @Override
    public void close() {
        super.close();
        this.audiences.close();
    }
}