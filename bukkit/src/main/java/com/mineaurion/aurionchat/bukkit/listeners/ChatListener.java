package com.mineaurion.aurionchat.bukkit.listeners;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.bukkit.AurionChatPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;

public class ChatListener implements Listener {
    private final AurionChat plugin;

    public ChatListener(AurionChat plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();

        if(!player.hasPermission("aurionchat.chat.speak")){
            event.setCancelled(true);
            return;
        }
        AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(player.getUniqueId());

        String currentChannel = aurionChatPlayer.getCurrentChannel();
        String messageFormat = AurionChat.utils.processMessage(currentChannel, event.getMessage(), aurionChatPlayer);

        try{
            this.plugin.getChatService().send(currentChannel,messageFormat);
        }
        catch(IOException e){
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        }

        event.setCancelled(true); // TODO: need to remove that. Need to adapt rabbitmq to a fanout exchange for the chat.
    }
}
