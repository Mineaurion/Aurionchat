package com.mineaurion.aurionchat.bukkit.listeners;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.bukkit.AurionChatPlayer;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;

public class ChatListener implements Listener {
    private AurionChat plugin;

    public ChatListener(AurionChat plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event){
        if( event.isCancelled()) {
            return;
        }
        AurionChatPlayer aurionChatPlayer = AurionChatPlayer.getOnlineAurionChatPlayer(event.getPlayer());
        String spy = "";

        if(!aurionChatPlayer.getPlayer().hasPermission("aurionchat.spy.override")){
            for(AurionChatPlayer p: AurionChat.onlinePlayers){
                if((p.isOnline()) && (p.isSpy())){
                    p.getPlayer().sendMessage(spy);
                }
            }
        }

        aurionChatPlayer.addListening("test");

        String evMessage = event.getMessage();
        String evChannel = aurionChatPlayer.getCurrentChannel();
        String messageFormat = plugin.getUtils().processMessage(evChannel,evMessage,event.getPlayer());

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
