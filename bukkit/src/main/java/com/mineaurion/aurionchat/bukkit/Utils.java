package com.mineaurion.aurionchat.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Set;

public class Utils {

    private AurionChat plugin;
    private Config config;

    public Utils(AurionChat plugin){
        this.plugin = plugin;
        this.config = plugin.getConfigPlugin();
    }

    public String processMessage(String channel, String message, Player player){
        String channelFormat = config.getFormatChannel(channel);
        if(!player.hasPermission("aurionchat.chat.colors")){
            message = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&',message));
        }
        return channelFormat
                .replace("{prefix}", AurionChat.chat.getPlayerPrefix(player))
                .replace("{suffix}", AurionChat.chat.getPlayerSuffix(player))
                .replace("{display_name}", player.getDisplayName())
                .replace("{message}", message);
    }

    public void sendMessageToPlayer(String channelName, String message){
        for(AurionChatPlayer p: AurionChat.onlinePlayers){
            Set<String> listening = p.getListening();
            Player player = p .getPlayer();
            if(listening.contains(channelName)){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
                if(message.toLowerCase().contains("@" + player.getDisplayName().toLowerCase() ) || message.toLowerCase().contains("@" + player.getName().toLowerCase())){
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                }
            }
        }
    }


    public void broadcastToPlayer(String channelName, String message){
        String channelPermission = config.getPermissionChannelAutomessage(channelName);
        for(Player player: plugin.getServer().getOnlinePlayers()){
            if(player.hasPermission(channelPermission)){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
            }
        }
    }
}
