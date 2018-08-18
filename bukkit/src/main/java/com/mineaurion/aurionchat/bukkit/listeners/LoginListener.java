package com.mineaurion.aurionchat.bukkit.listeners;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.bukkit.AurionChatPlayer;
import org.bukkit.Bukkit;
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

    public LoginListener(AurionChat main) {
        plugin = main;
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

        AurionChatPlayer aurionChatPlayer = AurionChatPlayer.getAurionChatPlayer(event.getPlayer());
        if(aurionChatPlayer == null){
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();
            String name = player.getName();
            Set<String> listening = new HashSet<>();
            String current = "global";
            listening.add(current);
            aurionChatPlayer = new AurionChatPlayer(uuid, name, current, listening, name, false);
            AurionChat.players.add(aurionChatPlayer);
        }
        aurionChatPlayer.setOnline(true);
        AurionChat.onlinePlayers.add(aurionChatPlayer);
        aurionChatPlayer.addListening("global");
        aurionChatPlayer.setCurrentChannel("global");
    }

    private void playerLeaving(Player player){
        AurionChatPlayer aurionChatPlayer = AurionChatPlayer.getAurionChatPlayer(player);
        if(aurionChatPlayer.getPlayer() == null){
            return;
        }
        aurionChatPlayer.setOnline(false);
        AurionChat.onlinePlayers.remove(aurionChatPlayer);
    }
}
