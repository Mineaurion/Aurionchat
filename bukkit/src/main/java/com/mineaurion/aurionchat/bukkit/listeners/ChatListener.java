package com.mineaurion.aurionchat.bukkit.listeners;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.Utils;
import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
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
        ConfigurationAdapter config = plugin.getConfigurationAdapter();
        Component messageFormat = Utils.processMessage(
                config.wrapString(player.getUniqueId(), config.getChannels().get(currentChannel).format),
                LegacyComponentSerializer.legacy('&').deserialize(event.getMessage()).asComponent(),
                aurionChatPlayer,
                plugin.getConfigurationAdapter().getChannels().get(currentChannel).urlMode
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
