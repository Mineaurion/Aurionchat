package com.mineaurion.aurionchat.bukkit;

import com.mineaurion.aurionchat.common.AurionChatPlayers;
import com.mineaurion.aurionchat.common.LuckPermsUtils;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.caching.UserData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class Utils {

    private AurionChat plugin;
    private Config config;
    private AurionChatPlayers aurionChatPlayers;
    private Optional<LuckPermsUtils> luckPermsUtils;

    public Utils(AurionChat plugin){
        this.plugin = plugin;
        this.config = plugin.getConfigPlugin();
        this.aurionChatPlayers = plugin.getAurionChatPlayers();
        this.luckPermsUtils = plugin.getLuckPermsUtils();
    }

    public String processMessage(String channel, String message, Player player){
        String channelFormat = config.getFormatChannel(channel);
        if(!player.hasPermission("aurionchat.chat.colors")){
            message = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&',message));
        }
        return channelFormat
                .replace("{prefix}", getPlayerPrefix(player).orElse(""))
                .replace("{suffix}", getPlayerSuffix(player).orElse(""))
                .replace("{display_name}", player.getDisplayName())
                .replace("{message}", message);
    }

    public Optional<String> getPlayerPrefix(Player player){
        Optional<String> prefix;
        if(luckPermsUtils.isPresent()){
            prefix = luckPermsUtils.get().getPlayerPrefix(player, player.getUniqueId());
        }
        else{
             prefix = Optional.ofNullable(AurionChat.chat.getPlayerPrefix(player));
        }
        return prefix;
    }

    public Optional<String> getPlayerSuffix(Player player){
        Optional<String> suffix;
        if(luckPermsUtils.isPresent()){
            suffix = luckPermsUtils.get().getPlayerSuffix(player, player.getUniqueId());
        }
        else{
            suffix = Optional.ofNullable(AurionChat.chat.getPlayerSuffix(player));
        }
        return suffix;
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
