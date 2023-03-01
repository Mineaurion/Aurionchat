package com.mineaurion.aurionchat.bukkit.listeners;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.bukkit.AurionChatPlayer;
import com.mineaurion.aurionchat.common.ChatService;
import com.mineaurion.aurionchat.common.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;

public class ChatListener implements Listener {

    private final AurionChat aurionChat;

    public ChatListener(AurionChat aurionChat){
        this.aurionChat = aurionChat;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();

        if(!player.hasPermission("aurionchat.chat.speak")){
            event.setCancelled(true);
            return;
        }
        AurionChatPlayer aurionChatPlayer = this.aurionChat.getAurionChatPlayers().get(player.getUniqueId());

        String currentChannel = aurionChatPlayer.getCurrentChannel();
        String messageFormat = Utils.processMessage(AurionChat.config.channels.get(currentChannel).format, event.getMessage(), aurionChatPlayer);

        try{
            ChatService.getInstance().send(currentChannel,messageFormat);
        }
        catch(IOException e){
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        }

        event.setCancelled(true); // TODO: need to remove that. Need to adapt rabbitmq to a fanout exchange for the chat.
    }
}
