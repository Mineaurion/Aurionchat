package com.mineaurion.aurionchat.bukkit;

import com.mineaurion.aurionchat.common.AurionChatPlayerCommon;
import com.mineaurion.aurionchat.common.LuckPermsUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class AurionChatPlayer extends AurionChatPlayerCommon<Player> {
    private static final LuckPermsUtils luckPermsUtils = AurionChat.luckPermsUtils;

    public AurionChatPlayer(Player player, Set<String> channels){
        super(player, AurionChat.config.rabbitmq.serverName, channels);
    }
    @Override
    public boolean hasPermission(String permission) {
        return this.player.hasPermission(permission);
    }

    @Override
    public void sendMessage(String message) {
        this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }

    @Override
    public String getPrefix() {
        return luckPermsUtils.getPlayerPrefix(this.getUniqueId()).orElse("");
    }

    @Override
    public String getSuffix() {
        return luckPermsUtils.getPlayerSuffix(this.getUniqueId()).orElse("");
    }

    @Override
    public String getDisplayName() {
        return this.player.getDisplayName();
    }

    @Override
    public void notifyPlayer(){
        this.player.playSound(player.getLocation(), AurionChat.config.options.sound, 1, 1);
    }
}
