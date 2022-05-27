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
        super(player, channels);
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
        return luckPermsUtils.getPlayerPrefix(this.getUniqueId());
    }

    @Override
    public String getSuffix() {
        return luckPermsUtils.getPlayerSuffix(this.getUniqueId());
    }

    @Override
    public String getDisplayName() {
        return this.player.getDisplayName();
    }

    @Override
    public void notifyPlayer(){
        this.player.playSound(player.getLocation(), AurionChat.config.getPingSound(), 1, 1);
    }
}
