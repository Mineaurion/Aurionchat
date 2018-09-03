package com.mineaurion.aurionchat.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;

public class Utils {

    private AurionChat plugin;

    public Utils(AurionChat plugin){
        this.plugin = plugin;
    }

    public String processMessage(String channel, String message, Player player){
        String channelFormat = plugin.getConfigPlugin().getFormatChannel(channel);
        return ChatColor.translateAlternateColorCodes('&',channelFormat.replace("{prefix}", AurionChat.chat.getPlayerPrefix(player))
                                            .replace("{suffix}", AurionChat.chat.getPlayerSuffix(player))
                                            .replace("{display_name}", player.getDisplayName())
                                            .replace("{message}", message));
    }
    public void sendMessageToPlayer(String channelName, String message){
        for(AurionChatPlayer p: AurionChat.onlinePlayers){
            Set<String> listening = p.getListening();
            if(listening.contains(channelName)){
               //Player player = p.getPlayer();
                p.getPlayer().sendMessage(message);
            }
        }
    }
}
