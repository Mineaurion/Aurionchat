package com.mineaurion.aurionchat.bukkit;

import com.mineaurion.aurionchat.common.AurionChatPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class Utils {

    private AurionChat plugin;
    private Config config;
    private AurionChatPlayers aurionChatPlayers;

    public Utils(AurionChat plugin){
        this.plugin = plugin;
        this.config = plugin.getConfigPlugin();
        this.aurionChatPlayers = plugin.getAurionChatPlayers();
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
        Set<UUID> playersListenChannel = aurionChatPlayers.getPlayersListeningChannel(channelName);
        for(UUID uuid: playersListenChannel){
            Player player = Bukkit.getPlayer(uuid);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
            if(message.toLowerCase().contains("@" + player.getDisplayName().toLowerCase() ) || message.toLowerCase().contains("@" + player.getName().toLowerCase())){
                player.playSound(player.getLocation(), config.getPingSound(), 1, 1);
            }
        }

    }

    public void broadcastToPlayer(String channelName, String message){
        String channelPermission = config.getPermissionChannelAutomessage(channelName);
        if(plugin.getServer().getOnlinePlayers().size() > 0){
            for(Player player: plugin.getServer().getOnlinePlayers()){
                if(player.hasPermission(channelPermission)){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
                }
            }
        }
    }
}
