package com.mineaurion.aurionchat.bukkit.listeners;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.common.AurionChatPlayers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LoginListener implements Listener {
    private AurionChat plugin;
    private AurionChatPlayers aurionChatPlayers;

    public LoginListener(AurionChat plugin) {
        this.plugin = plugin;
        this.aurionChatPlayers = plugin.getAurionChatPlayers();
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
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Set<String> listenChannel = new HashSet<String>();
        String currentChannel = "global";

        for( String channel:plugin.getConfigPlugin().getAllChannel()){
            if(player.hasPermission("aurionchat.joinchannel." + channel)){
                currentChannel = channel;
            }
            if(player.hasPermission("aurionchat.listenchannel." + channel)){
                listenChannel.add(channel);
            }
        }
        aurionChatPlayers.addPlayer(uuid, listenChannel, currentChannel);

    }

    private void playerLeaving(Player player){
        aurionChatPlayers.removePlayer(player.getUniqueId());
    }
}
