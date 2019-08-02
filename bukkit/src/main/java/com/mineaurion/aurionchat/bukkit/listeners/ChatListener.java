package com.mineaurion.aurionchat.bukkit.listeners;

import com.mineaurion.aurionchat.bukkit.AurionChat;

import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.AurionChatPlayers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;

public class ChatListener implements Listener {
    private AurionChat plugin;
    private AurionChatPlayers aurionChatPlayers;

    public ChatListener(AurionChat plugin){
        this.plugin = plugin;
        this.aurionChatPlayers = plugin.getAurionChatPlayers();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event){
        if( event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        AurionChatPlayer aurionChatPlayer = aurionChatPlayers.getAurionChatPlayer(player.getUniqueId());
        String spy = "";

//        if(!player.hasPermission("aurionchat.spy.override")){
//            for(AurionChatPlayer p: AurionChat.onlinePlayers){
//                if((p.isOnline()) && (p.isSpy())){
//                    p.getPlayer().sendMessage(spy);
//                }
//            }
//        }
        String evMessage = event.getMessage();
        String evChannel = aurionChatPlayer.getCurrentChannel();
        String messageFormat = plugin.getUtils().processMessage(evChannel, evMessage, player);

        try{
            plugin.getChatService().send(evChannel,messageFormat);
        }
        catch(IOException e){
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        }

        event.setCancelled(true);
        return;

    }
}
