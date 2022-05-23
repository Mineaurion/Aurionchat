package com.mineaurion.aurionchat.bukkit.listeners;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.bukkit.Config;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class LoginListener implements Listener {

    private final Map<UUID, AurionChatPlayer> aurionChatPlayers;

    public LoginListener() {
        this.aurionChatPlayers = AurionChat.aurionChatPlayers;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerKick(PlayerKickEvent plog){
        playerLeaving(plog.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent plog){
        playerLeaving(plog.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event){
        Config config = AurionChat.config;
        Player player = event.getPlayer();
        Set<String> listenChannel = new HashSet<>(Arrays.asList("global", config.getServername()));
        String currentChannel = "global";

        for( String channel: config.getAllChannel()){
            if(player.hasPermission("aurionchat.joinchannel." + channel)){
                listenChannel.add(channel);
                currentChannel = channel;
            }
            if(player.hasPermission("aurionchat.listenchannel." + channel)){
                listenChannel.add(channel);
            }
        }
        AurionChatPlayer aurionChatPlayer = new AurionChatPlayer(listenChannel, currentChannel);
        this.aurionChatPlayers.putIfAbsent(event.getPlayer().getUniqueId(), aurionChatPlayer);

    }

    private void playerLeaving(Player player){
        this.aurionChatPlayers.remove(player.getUniqueId());
    }
}
