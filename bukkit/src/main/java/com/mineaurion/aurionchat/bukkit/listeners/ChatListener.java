package com.mineaurion.aurionchat.bukkit.listeners;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.bukkit.AurionChatPlayer;

import com.mineaurion.aurionchat.bukkit.Config;

import com.mineaurion.aurionchat.bukkit.utilities.Format;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;
import java.util.Iterator;

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

        String filtered = event.getMessage();
        String spy = "";

        if(aurionChatPlayer.getPlayer().hasPermission("aurionchat.color")) {
            filtered = Format.FormatStringColor(filtered);
        }
        if(aurionChatPlayer.getPlayer().hasPermission("aurionchat.format")){
            filtered = Format.FormatString(filtered);
        }

        filtered = " " + filtered;

        if(!aurionChatPlayer.getPlayer().hasPermission("aurionchat.spy.override")){
            for(AurionChatPlayer p: AurionChat.onlinePlayers){
                if((p.isOnline()) && (p.isSpy())){
                    p.getPlayer().sendMessage(spy);
                }
            }
        }

        aurionChatPlayer.addListening("test");


        String evMessage = event.getMessage();
        String eventChannel = aurionChatPlayer.getCurrentChannel();
        String chatFormat = plugin.getConfigPlugin().getFormatChannel(eventChannel);
        String channelFormat = chatFormat.replace("{prefix}","testPrefix")
                                  .replace("{display_name}",event.getPlayer().getDisplayName())
                                  .replace("{message}",evMessage);

        try{
            plugin.getChatService().send(eventChannel,channelFormat);
        }
        catch(IOException e){
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        }

        event.setCancelled(true);
        return;

    }
}
