package com.mineaurion.aurionchat.bukkit.listeners;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.ChatService;
import com.mineaurion.aurionchat.common.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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

        AurionChatPlayer aurionChatPlayer = plugin.getAurionChatPlayers().get(player.getUniqueId());
        if(!aurionChatPlayer.canSpeak()){
            event.setCancelled(true);
            return;
        }

        String currentChannel = aurionChatPlayer.getCurrentChannel();
        Component messageFormat = Utils.processMessage(
                plugin.getConfigurationAdapter().getChannels().get(currentChannel).format,
                LegacyComponentSerializer.legacy('&').deserialize(event.getMessage()).asComponent(),
                aurionChatPlayer,
                Utils.URL_MODE_ALLOW
        );

        try{
            plugin.getChatService().send(currentChannel, messageFormat);
        }
        catch(IOException e){
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        }

        event.setCancelled(true); // TODO: need to remove that. Need to adapt rabbitmq to a fanout exchange for the chat.
    }
}
