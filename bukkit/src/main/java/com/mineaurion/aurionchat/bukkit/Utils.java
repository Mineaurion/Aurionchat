package com.mineaurion.aurionchat.bukkit;

import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.LuckPermsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;

public class Utils {
    private static final Config config = AurionChat.config;
    private static final LuckPermsUtils luckPermsUtils = AurionChat.luckPermsUtils;

    public static String processMessage(String channel, String message, Player player){
        String channelFormat = config.getFormatChannel(channel);
        if(!player.hasPermission("aurionchat.chat.colors")){
            message = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&',message));
        }
        return channelFormat
                .replace("{prefix}", getPlayerPrefix(player))
                .replace("{suffix}", getPlayerSuffix(player))
                .replace("{display_name}", player.getDisplayName())
                .replace("{message}", message);
    }

    public static String getPlayerPrefix(Player player){
        return luckPermsUtils != null ? luckPermsUtils.getPlayerPrefix(player.getUniqueId()) : AurionChat.chat.getPlayerPrefix(player);
    }

    public static String getPlayerSuffix(Player player){
        return luckPermsUtils != null ? luckPermsUtils.getPlayerSuffix(player.getUniqueId()) : AurionChat.chat.getPlayerSuffix(player);
    }


    public static void sendMessageToPlayer(String channelName, String message){
        Bukkit.getOnlinePlayers().forEach(player -> {
            AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(player.getUniqueId());
            if(aurionChatPlayer.getChannels().contains(channelName)){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
                if(message.toLowerCase().contains("@" + player.getDisplayName().toLowerCase() ) || message.toLowerCase().contains("@" + player.getName().toLowerCase())){
                    player.playSound(player.getLocation(), config.getPingSound(), 1, 1);
                }
            }
        });
    }

    public static void broadcastToPlayer(String channelName, String message){
        Collection<? extends Player> playerList = Bukkit.getOnlinePlayers();
        if(playerList.size() > 0){
            playerList.forEach(player -> {
                if(player.hasPermission("aurionchat.automessage." + channelName)){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
                }
            });
        }
    }
}
