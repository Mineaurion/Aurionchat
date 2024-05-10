package com.mineaurion.aurionchat.bukkit.listeners;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.common.listeners.LoginListenerCommon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LoginListener extends LoginListenerCommon<AurionChat> implements Listener {

    public LoginListener(AurionChat plugin){
        super(plugin);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerKick(PlayerKickEvent event){
        playerLeaving(plugin.getSenderFactory().wrap(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent event){
        playerLeaving(plugin.getSenderFactory().wrap(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event){
        playerJoin(plugin.getSenderFactory().wrap(event.getPlayer()));
    }
}
