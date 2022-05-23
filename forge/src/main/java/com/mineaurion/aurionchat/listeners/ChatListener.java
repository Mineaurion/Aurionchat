package com.mineaurion.aurionchat.listeners;

import com.mineaurion.aurionchat.AurionChat;
import com.mineaurion.aurionchat.AurionChatPlayer;
import com.mineaurion.aurionchat.Utils;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
public class ChatListener {

    private final AurionChat plugin;

    public ChatListener(AurionChat plugin)
    {
        this.plugin = plugin;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void onServerChatEvent(ServerChatEvent event) {

        AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(event.getPlayer().getUUID());
        String currentChannel = aurionChatPlayer.getCurrentChannel();
        String messageFormat = Utils.processMessage(currentChannel, event.getMessage(), event.getPlayer());
        try {
            this.plugin.getChatService().send(currentChannel, messageFormat);
        } catch (IOException e) {
            LogManager.getLogger().error(e.getMessage());
        }
        event.setCanceled(true); // TODO: need to remove that. Need to adapt rabbitmq to a fanout exchange for the chat.
    }
}

