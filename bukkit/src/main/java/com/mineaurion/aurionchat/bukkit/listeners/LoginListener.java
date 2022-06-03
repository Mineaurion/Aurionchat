package com.mineaurion.aurionchat.bukkit.listeners;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.bukkit.AurionChatPlayer;
import com.mineaurion.aurionchat.common.listeners.LoginListenerCommon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;

public class LoginListener extends LoginListenerCommon<AurionChatPlayer> implements Listener {

    private static final Map<UUID, AurionChatPlayer> aurionChatPlayers = AurionChat.aurionChatPlayers;

    public LoginListener() {
        super(aurionChatPlayers);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerKick(PlayerKickEvent event){
        this.playerLeaving(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent event){
        playerLeaving(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event){
        aurionChatPlayers.putIfAbsent(
                event.getPlayer().getUniqueId(),
                new AurionChatPlayer(event.getPlayer(), AurionChat.config.channels.keySet())
        );
    }
}
